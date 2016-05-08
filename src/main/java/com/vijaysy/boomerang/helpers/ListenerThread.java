package com.vijaysy.boomerang.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.Async;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.core.restclient.RestClient;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by vijaysy on 09/04/16.
 */

@Slf4j
public class ListenerThread implements Runnable {
    private final String channel;
    private final Jedis jedis;
    private final MangedCache mangedCache;
    private final RetryItemDao retryItemDao;
    private final IngestionService ingestionService;
    private final HashMap<HttpMethod, RestClient> httpMethodRestClientHashMap;
    private final ObjectMapper objectMapper;


    public ListenerThread(MangedCache mangedCache, String channel, RetryItemDao retryItemDao, IngestionService ingestionService, HashMap<HttpMethod, RestClient> httpMethodRestClientHashMap, ObjectMapper objectMapper) {
        this.mangedCache = mangedCache;
        this.channel = "__key*__:" + channel + ".*";
        this.retryItemDao = retryItemDao;
        this.ingestionService = ingestionService;
        this.jedis = mangedCache.getJedisResource();
        this.httpMethodRestClientHashMap = httpMethodRestClientHashMap;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        jedis.psubscribe(new JedisPubSub() {

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
                log.info("onPSubscribe " + pattern + " " + subscribedChannels);
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
                log.info("[Pattern:" + pattern + "]\t" + "[Channel: " + channel + "]\t" + "[Message: " + message + "]");
                if (!message.equals("expired")) return;
                String messageId = channel.substring(channel.indexOf('.') + 1);
                RetryItem retryItem;
                try (Jedis jedis = mangedCache.getJedisResource()) {
                    if (jedis.setnx(messageId, messageId) == 1) {

                        if (Objects.isNull(retryItem = retryItemDao.get(messageId))) return;
                        if (retryItem.getNextRetry() >= retryItem.getMaxRetry()) {
                            process(retryItem, FallBackReasons.MAX_RETRY, Response.status(400).build());
                            return;
                        }
                        jedis.expire(messageId, 20);
                        Response response = httpMethodRestClientHashMap.get(retryItem.getHttpMethod()).invoke(retryItem.getHttpUri(), retryItem.getMessage(), getHeaders(retryItem.getHeaders()), retryItem.getMessageId());
                        if (Response.Status.Family.SUCCESSFUL.equals(response.getStatus()))
                            process(retryItem, FallBackReasons.SUCCESSFULL, response);
                        else if (response.getStatus() == retryItem.getRetryStatusCode())
                            ingestionService.againProcess(retryItem);
                        else
                            process(retryItem, FallBackReasons.NOT_RETRY_STATUS_CODE, response);

                    }
                } catch (Exception e) {
                    log.error("Exception " + e.toString());
                }
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                super.onPUnsubscribe(pattern, subscribedChannels);
            }

        }, channel);

    }

    @Async
    private void process(RetryItem retryItem, FallBackReasons fallBackReasons, Response response) throws DBException {
        retryItem.setFallBackReasons(fallBackReasons);
        retryItem.setProcessed(true);
        boolean flag = fallBackReasons.equals(FallBackReasons.SUCCESSFULL);

        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("retry", flag);
        headers.putSingle("reason", retryItem.getFallBackReasons());
        Object body = (retryItem.getNeedResponse() && flag) ? response : "";
        Response returnResponse = httpMethodRestClientHashMap.get(HttpMethod.PUT).invoke(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback", body, headers, "executeFallBack");

        retryItem.setReturnFlag(Response.Status.Family.SUCCESSFUL.equals(returnResponse));
        retryItemDao.update(retryItem);
    }

    private MultivaluedHashMap<String, Object> getHeaders(String headers) {
        try {
            return (headers != null && !headers.isEmpty()) ? objectMapper.readValue(headers, MultivaluedHashMap.class) : null;
        } catch (IOException e) {
            log.error("Exception while deserialization the headers");
            return null;

        }
    }

}
