package com.vijaysy.boomerang.listeners;

import com.vijaysy.boomerang.Boomerang;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.JerseyClient;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
public class SubListenerThread implements Runnable {

    private String host;
    private int port;
    private String channel;
    private Jedis jedis ;
    private volatile Boolean flag;

    public SubListenerThread(){
        this.host="localhost";
        this.port=26379;
        this.channel="__key*__:RT*";
        this.flag=true;
        jedis= new Jedis(host);
    }


    public SubListenerThread(String host,int port,String channel){
        this.host=host;
        this.port=port;
        this.channel="__key*__:"+channel+".*";
        this.flag=true;
        jedis= new Jedis(host);
    }


    @Override
    public void run() {
        while (flag){
            jedis.psubscribe(new JedisPubSub() {
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    log.info("[Pattern:" + pattern + "]\t"+"[Channel: " + channel + "]\t"+"[Message: " + message + "]");
                    if(!message.equals("expired")) return;
                    String messageId=channel.substring(channel.indexOf('.')+1);
                    try {
                        JedisPool pool = new JedisPool(new JedisPoolConfig(),host);
                        Jedis jedis = pool.getResource();
                        if(jedis.setnx(messageId,messageId)==1){
                            RetryItem retryItem = Boomerang.readRetryItem(messageId);
                            jedis.expire(messageId,20);
                            if(Objects.isNull(retryItem)) return;
                            boolean f = (retryItem.getNextRetry()!=retryItem.getMaxRetry())?new JerseyClient(retryItem).execute():new JerseyClient(retryItem).executeFallBack();
                            //TODO: handle failed executeFallBack method
                        }

                    }catch (Exception e){
                        log.error("Exception while taking lock");
                        return;
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
