package com.vijaysy.boomerang.listeners;

import com.vijaysy.boomerang.Boomerang;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.Cache;
import com.vijaysy.boomerang.utils.JerseyClient;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Objects;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
public class SubListenerThread implements Runnable {

    private String channel;
    private Jedis jedis ;
    private Cache cache;
    private JedisSentinelPool jedisSentinelPool;
    private volatile Boolean flag;


    private SubListenerThread(){}

    public SubListenerThread(Cache cache, String channel){
        this.channel="__key*__:"+channel+".*";
        this.flag=true;
        this.cache=cache;
        jedis= cache.getJedisResource();
    }


    @Override
    public void run() {
        while (flag){
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
                    try {
                        if(jedis.setnx(messageId,messageId)==1){
                            RetryItem retryItem = Boomerang.readRetryItem(messageId);
                            jedis.expire(messageId,20);
                            if(Objects.isNull(retryItem)) return;
                            JerseyClient jerseyClient = new JerseyClient(retryItem);
                            boolean f = (retryItem.getNextRetry()!=retryItem.getMaxRetry())? jerseyClient.execute(): jerseyClient.executeFallBack();
                            //TODO: handle failed executeFallBack method
                        }
                    }catch (Exception e){
                        log.error("JerseyClient exception or LockException");
                    }finally {
                        jedis.del(messageId);
                        jedis.close();
                    }
                }
            }, channel);
        }

    }

    public void stopListener(){
        this.flag=false;
    }




}
