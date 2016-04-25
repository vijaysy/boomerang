package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import javax.ws.rs.core.Response;
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
    private final JerseyClient jerseyClient;
    private final IngestionService ingestionService;


    public ListenerThread(MangedCache mangedCache, String channel, RetryItemDao retryItemDao, JerseyClient jerseyClient, IngestionService ingestionService) {
        this.mangedCache = mangedCache;
        this.channel = "__key*__:" + channel + ".*";
        this.retryItemDao = retryItemDao;
        this.jerseyClient = jerseyClient;
        this.ingestionService = ingestionService;
        this.jedis = mangedCache.getJedisResource();
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
                        Response response = jerseyClient.execute(retryItem);
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

    private void process(RetryItem retryItem, FallBackReasons fallBackReasons, Response response) throws DBException {
        retryItem.setFallBackReasons(fallBackReasons);
        retryItem.setProcessed(true);
        retryItemDao.update(retryItem);
        retryItem.setReturnFlag(Response.Status.Family.SUCCESSFUL.equals(jerseyClient.returnExecute(retryItem, response, fallBackReasons.equals(FallBackReasons.SUCCESSFULL))));
        retryItemDao.update(retryItem);

    }

}
