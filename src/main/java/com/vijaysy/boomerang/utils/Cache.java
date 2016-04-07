package com.vijaysy.boomerang.utils;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * Created by vijaysy on 07/04/16.
 */
@Slf4j
@Singleton
public class Cache {

    private String master ;
    private String sentinels;
    private JedisSentinelPool jedisSentinelPool = null;
    private int timeout;
    private String password;
    private int db;

    private Cache(){}

    public Cache(String master,String sentinels,int timeout,String password,int db) {
        this.master=master;
        this.sentinels=sentinels;
        this.timeout=timeout;
        this.password=password;
        this.db=db;
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        jedisSentinelPool = new JedisSentinelPool(this.master, Sets.newHashSet(this.sentinels.split(",")), poolConfig, this.timeout, this.password, this.db);

    }

    public Jedis getJedisResource() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        this.jedisSentinelPool = new JedisSentinelPool(master, Sets.newHashSet(sentinels.split(",")), poolConfig, timeout, password, db);
        return this.jedisSentinelPool.getResource();
    }

    public void ping() throws Exception {
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            jedis.ping();
        }
    }

    public void destroy() {
        if (!Objects.isNull(jedisSentinelPool)) jedisSentinelPool.destroy();
    }

}
