package com.vijaysy.boomerang.listeners;

import com.vijaysy.boomerang.Boomerang;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.JerseyClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by vijay.yala on 02/04/16.
 */
public class SubListenerThread implements Runnable {

    private String host;
    private String port;
    private String channel;
    private Jedis jedis ;
    private volatile Boolean flag;

    public SubListenerThread(){
        this.host="localhost";
        this.port="26379";
        this.channel="__key*__:RT*";
        this.flag=true;
        jedis= new Jedis(host);
    }


    public SubListenerThread(String host,String port,String channel){
        this.host=host;
        this.port=port;
        this.channel=channel;
        this.flag=true;
        jedis= new Jedis(host);
    }


    @Override
    public void run() {
        System.out.printf("Started\n");
        while (flag){
            jedis.psubscribe(new JedisPubSub() {
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    System.out.print("[Pattern:" + pattern + "]");
                    System.out.print("[Channel: " + channel + "]");
                    System.out.println("[Message: " + message + "]");
                    if(!message.equals("expired")) return;
                    System.out.printf("Got expired Call\n");
                    String messageId=channel.substring(17);
                    RetryItem retryItem = Boomerang.readRetryItem(messageId);
                    boolean f = (retryItem.getNextRetry()!=retryItem.getMaxRetry())?new JerseyClient(retryItem).execute():new JerseyClient(retryItem).executeFallBack();
                }
            }, channel);
        }

    }

    public void stopListener(){
        this.flag=false;
    }




}
