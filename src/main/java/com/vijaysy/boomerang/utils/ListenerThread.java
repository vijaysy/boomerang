package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemListenerDAO;
import com.vijaysy.boomerang.models.RetryItem;
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
    private String channel;
    private Jedis jedis ;
    private Cache cache;
    private final RetryItemListenerDAO retryItemListenerDAO;
    private final JerseyClient jerseyClient;



    public ListenerThread(Cache cache , String channel, RetryItemListenerDAO retryItemDAO, JerseyClient jerseyClient){
        this.cache=cache;
        this.channel="__key*__:"+channel+".*";
        this.retryItemListenerDAO =retryItemDAO;
        this.jerseyClient=jerseyClient;
        this.jedis=cache.getJedisResource();
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
                log.info("[Pattern:" + pattern + "]\t"+"[Channel: " + channel + "]\t"+"[Message: " + message + "]");
                if(!message.equals("expired")) return;
                String messageId=channel.substring(channel.indexOf('.')+1);
                Jedis jedis=cache.getJedisResource();
                if(jedis.setnx(messageId,messageId)==1){
                    RetryItem retryItem = retryItemListenerDAO.get(messageId);
                    jedis.expire(messageId,20);
                    if(Objects.isNull(retryItem)) return;
                    Response f = (retryItem.getNextRetry()!=retryItem.getMaxRetry())? jerseyClient.execute(retryItem): jerseyClient.executeFallBack(retryItem);
                    //TODO: handle failed executeFallBack method
                }
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                super.onPUnsubscribe(pattern, subscribedChannels);
            }

        }, channel);
    }

}
