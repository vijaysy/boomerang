package com.vijaysy.boomerang.helpers;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.services.RetryItemHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by vijaysy on 09/04/16.
 */

@Slf4j
public class ListenerThread implements Runnable {
    private final String channel;
    private final Jedis jedis;
    private final MangedCache mangedCache;
    private final RetryItemHandler retryItemHandler;
    static final String key = "__key*__:";
    static final String pattern = ".*";


    @Inject
    public ListenerThread(MangedCache mangedCache, String channel, RetryItemHandler retryItemHandler) {
        this.mangedCache = mangedCache;
        this.retryItemHandler = retryItemHandler;
        this.channel = key + channel + pattern;
        this.jedis = mangedCache.getJedisResource();
    }

    @Override
    public void run() {
        jedis.psubscribe(new JedisPubSub() {
            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
                log.info("onPSubscribe: [Pattern: %s ]  [SubscribedChannels: %d]", pattern, subscribedChannels);
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
                log.info("[Pattern: %s ]    [Channel: %s]   [Message: %s]", pattern, channel, message);
                if (!message.equals("expired")) return;
                String messageId = channel.substring(channel.indexOf('.') + 1);
                try (Jedis jedis = mangedCache.getJedisResource()) {
                    if (jedis.setnx(messageId, messageId) == 1)
                        retryItemHandler.handelRetryItem(messageId);
                    jedis.del(messageId);
                }
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                super.onPUnsubscribe(pattern, subscribedChannels);
            }

        }, channel);

    }
}
