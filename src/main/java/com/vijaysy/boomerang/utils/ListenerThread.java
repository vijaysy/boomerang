package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.FallBackReasons;
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
    private final Cache cache;
    private final RetryItemDao retryItemDao;
    private final JerseyClient jerseyClient;
    private final IngestionService ingestionService;


    public ListenerThread(Cache cache, String channel, RetryItemDao retryItemDao, JerseyClient jerseyClient, IngestionService ingestionService) {
        this.cache = cache;
        this.channel = "__key*__:" + channel + ".*";
        this.retryItemDao = retryItemDao;
        this.jerseyClient = jerseyClient;
        this.ingestionService = ingestionService;
        this.jedis = cache.getJedisResource();
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
                try (Jedis jedis = cache.getJedisResource()) {
                    if (jedis.setnx(messageId, messageId) == 1) {

                        if (Objects.isNull(retryItem = retryItemDao.get(messageId))) return;

                        if (retryItem.getNextRetry() >= retryItem.getMaxRetry()) {
                            jerseyClient.executeFallBack(retryItem, FallBackReasons.MAX_RETRY);
                            return;
                        }

                        jedis.expire(messageId, 20);
                        Response response = jerseyClient.execute(retryItem);

                        if (Response.Status.Family.SUCCESSFUL.equals(response.getStatus())) {
                            jerseyClient.executeSuccess(retryItem, response);
                            return;
                        }

                        if (response.getStatus() == retryItem.getRetryStatusCode())
                            ingestionService.process(retryItem);
                        else
                            jerseyClient.executeFallBack(retryItem, FallBackReasons.NOT_RETRY_STATUS_CODE);

                    }
                }
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                super.onPUnsubscribe(pattern, subscribedChannels);
            }

        }, channel);
    }

}
