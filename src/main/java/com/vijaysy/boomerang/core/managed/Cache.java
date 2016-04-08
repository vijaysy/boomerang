package com.vijaysy.boomerang.core.managed;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
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

    private JedisSentinelPool jedisSentinelPool;

    @Inject
    public Cache(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool=jedisSentinelPool;
    }

    public Cache(String master,String sentinels,int timeout,String password,int db) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        jedisSentinelPool = new JedisSentinelPool(master, Sets.newHashSet(sentinels.split(",")), poolConfig,timeout,password,db);

    }

    public Jedis getJedisResource() {
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
