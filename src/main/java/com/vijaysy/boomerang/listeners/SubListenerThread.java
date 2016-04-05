package com.vijaysy.boomerang.listeners;

import com.vijaysy.boomerang.Boomerang;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.JerseyClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

/**
 * Created by vijaysy on 02/04/16.
 */
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
        //log.info("Listener Started");
        while (flag){
            jedis.psubscribe(new JedisPubSub() {
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
//                    log.info("[Pattern:" + pattern + "]");
//                    log.info("[Channel: " + channel + "]");
//                    log.info("[Message: " + message + "]");
                    if(!message.equals("expired")) return;
                    //log.info("Got expired Call");
                    String messageId=channel.substring(channel.indexOf('.')+1);
                    RetryItem retryItem = Boomerang.readRetryItem(messageId);
                    if(Objects.isNull(retryItem)) return;
                    boolean f = (retryItem.getNextRetry()!=retryItem.getMaxRetry())?new JerseyClient(retryItem).execute():new JerseyClient(retryItem).executeFallBack();
                    //TODO: handle failed executeFallBack method
                }
            }, channel);
        }

    }

    public void stopListener(){
        this.flag=false;
    }




}
