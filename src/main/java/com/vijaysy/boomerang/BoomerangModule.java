package com.vijaysy.boomerang;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.core.restclient.impl.GetClient;
import com.vijaysy.boomerang.core.restclient.impl.PostClient;
import com.vijaysy.boomerang.core.restclient.impl.PutClient;
import com.vijaysy.boomerang.core.restclient.RestClient;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.dao.RetryItemDaoImpl;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.services.ListenerService;
import com.vijaysy.boomerang.services.RetryItemHandler;
import com.vijaysy.boomerang.services.impl.IngestionServiceImpl;
import com.vijaysy.boomerang.services.impl.ListenerServiceImpl;
import com.vijaysy.boomerang.services.impl.RetryItemHandlerImpl;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 08/04/16.
 */
public class BoomerangModule extends AbstractModule {

    private HibernateBundle<BoomerangConfiguration> hibernateBundle;

    public BoomerangModule(HibernateBundle<BoomerangConfiguration> HibernateBundle) {
        this.hibernateBundle = HibernateBundle;
    }

    @Override
    protected void configure() {
        bind(MangedCache.class).in(Singleton.class);
        bind(IngestionService.class).to(IngestionServiceImpl.class).in(Singleton.class);
        bind(ListenerService.class).to(ListenerServiceImpl.class).in(Singleton.class);
        bind(RetryItemDao.class).to(RetryItemDaoImpl.class).in(Singleton.class);
        bind(RetryItemHandler.class).to(RetryItemHandlerImpl.class);

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
        return new JedisSentinelPool(boomerangConfiguration.getCacheConfig().getMaster(), Sets.newHashSet(boomerangConfiguration.getCacheConfig().getSentinels().split(",")), poolConfig, boomerangConfiguration.getCacheConfig().getTimeout(), boomerangConfiguration.getCacheConfig().getPassword(), boomerangConfiguration.getCacheConfig().getDb());
    }


    @Provides
    @Singleton
    public ThreadPoolExecutor getThreadPoolExecutor(BoomerangConfiguration boomerangConfiguration, Environment environment) {
        return (ThreadPoolExecutor) environment.lifecycle().executorService("RedisListeners-%d")
                .maxThreads(boomerangConfiguration.getThreadPoolSize())
                .build();
    }

    @Provides
    @Singleton
    public HashMap<HttpMethod, RestClient> getDictionary() {
        HashMap<HttpMethod, RestClient> httpMethodRestClientHashMap = new HashMap<>();
        httpMethodRestClientHashMap.put(HttpMethod.GET, new GetClient(getClient()));
        httpMethodRestClientHashMap.put(HttpMethod.POST, new PostClient(getClient()));
        httpMethodRestClientHashMap.put(HttpMethod.PUT, new PutClient(getClient()));
        return httpMethodRestClientHashMap;
    }

    @Provides
    @Singleton
    public Client getClient() {
        return ClientBuilder.newClient();
    }
}
