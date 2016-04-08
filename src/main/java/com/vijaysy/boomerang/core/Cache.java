package com.vijaysy.boomerang.core;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * Created by vijaysy on 07/04/16.
 */
@Slf4j
@Singleton
public class Cache implements Managed {

    private JedisSentinelPool jedisSentinelPool;

    @Inject
    public Cache(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool=jedisSentinelPool;
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

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping Jedis Sentinel Pool");
        destroy();

    }
}
