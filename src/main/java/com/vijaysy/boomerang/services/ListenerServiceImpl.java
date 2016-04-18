package com.vijaysy.boomerang.services;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemListenerDAO;
import com.vijaysy.boomerang.models.Config.ThreadConfig;
import com.vijaysy.boomerang.utils.JerseyClient;
import com.vijaysy.boomerang.utils.ListenerThread;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 11/04/16.
 */
@Slf4j
@Singleton
public class ListenerServiceImpl implements ListenerService {

    private final JerseyClient jerseyClient;
    private final RetryItemListenerDAO retryItemListenerDAO;
    private final Cache cache;
    private final IngestionService ingestionService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public ListenerServiceImpl(JerseyClient jerseyClient, RetryItemListenerDAO retryItemListenerDAO, Cache cache,IngestionService ingestionService,ThreadPoolExecutor threadPoolExecutor){
        this.cache=cache;
        this.retryItemListenerDAO=retryItemListenerDAO;
        this.jerseyClient=jerseyClient;
        this.ingestionService=ingestionService;
        this.threadPoolExecutor=threadPoolExecutor;

    }

    @Override
    public void createListener(ThreadConfig threadConfig) {
        for (int i=0;i< threadConfig.getListenerCount();i++) {
            log.info("Creating thread "+i+" of name "+threadConfig.getName());
            threadPoolExecutor.execute(new ListenerThread(cache, threadConfig.getChannel(), retryItemListenerDAO, jerseyClient, ingestionService));
        }

    }
}
