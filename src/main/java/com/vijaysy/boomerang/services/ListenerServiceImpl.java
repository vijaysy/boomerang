package com.vijaysy.boomerang.services;

import com.google.inject.Inject;
import com.vijaysy.boomeranglistener.core.Cache;
import com.vijaysy.boomeranglistener.dao.RetryItemDAO;
import com.vijaysy.boomeranglistener.helpers.JerseyClient;
import com.vijaysy.boomeranglistener.helpers.ListenerThread;
import com.vijaysy.boomeranglistener.models.ThreadConfig;

import java.util.concurrent.ExecutorService;

/**
 * Created by vijaysy on 11/04/16.
 */
public class ListenerServiceImpl implements ListenerService {

    private final JerseyClient jerseyClient;
    private final RetryItemDAO retryItemDAO;
    private final Cache cache;
    private final ExecutorService executorService;

    @Inject
    public ListenerServiceImpl(JerseyClient jerseyClient, RetryItemDAO retryItemDAO, Cache cache, ExecutorService executorService){
        this.cache=cache;
        this.retryItemDAO=retryItemDAO;
        this.jerseyClient=jerseyClient;
        this.executorService=executorService;

    }

    @Override
    public void createListener(ThreadConfig threadConfig) {
        new Thread(new ListenerThread(cache,threadConfig.getChannel(),retryItemDAO,jerseyClient)).start();
        //executorService.submit(new ListenerThread(jedis,threadConfig.getChannel(),retryItemDAO,jerseyClient));
    }
}
