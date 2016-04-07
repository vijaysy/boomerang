package com.vijaysy.boomerang;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.models.Config.CacheConfig;
import com.vijaysy.boomerang.utils.Cache;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Created by vijaysy on 08/04/16.
 */
public class BoomerangModule extends AbstractModule{

    private CacheConfig cacheConfig;

    public BoomerangModule(CacheConfig cacheConfig){
        this.cacheConfig=cacheConfig;
    }

    @Override
    protected void configure() {
        bind(Cache.class).in(Singleton.class);

    }

    @Provides
    @Singleton
    private JedisSentinelPool getJedisSentinelPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(cacheConfig.getMaxThreads());
        return new JedisSentinelPool(cacheConfig.getMaster(), Sets.newHashSet(cacheConfig.getSentinels().split(",")), poolConfig, cacheConfig.getTimeout(), cacheConfig.getPassword(), cacheConfig.getDb());
    }
}
