package com.vijaysy.boomerang;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.services.IngestionServiceImpl;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Created by vijaysy on 08/04/16.
 */
public class BoomerangModule extends AbstractModule{

    private HibernateBundle<BoomerangConfiguration> hibernateBundle;

    public BoomerangModule(HibernateBundle<BoomerangConfiguration> HibernateBundle){
        this.hibernateBundle=HibernateBundle;
    }

    @Override
    protected void configure() {
        bind(Cache.class).in(Singleton.class);
        bind(IngestionService.class).to(IngestionServiceImpl.class).in(Singleton.class);

    }

    @Singleton
    @Provides
    public SessionFactory provideSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    private JedisSentinelPool getJedisSentinelPool(BoomerangConfiguration boomerangConfiguration) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(boomerangConfiguration.getCacheConfig().getMaxThreads());
        return new JedisSentinelPool(boomerangConfiguration.getCacheConfig().getMaster(), Sets.newHashSet(boomerangConfiguration.getCacheConfig().getSentinels().split(",")), poolConfig, boomerangConfiguration.getCacheConfig().getTimeout(), boomerangConfiguration.getCacheConfig().getPassword(),boomerangConfiguration.getCacheConfig().getDb());
    }
}
