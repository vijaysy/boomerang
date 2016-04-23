package com.vijaysy.boomerang.services.impl;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.models.config.ThreadConfig;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.services.ListenerService;
import com.vijaysy.boomerang.utils.JerseyClientImpl;
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

    private final JerseyClientImpl jerseyClient;
    private final RetryItemDao retryItemDao;
    private final MangedCache mangedCache;
    private final IngestionService ingestionService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public ListenerServiceImpl(JerseyClientImpl jerseyClient, RetryItemDao retryItemDao, MangedCache mangedCache, IngestionService ingestionService, ThreadPoolExecutor threadPoolExecutor){
        this.mangedCache = mangedCache;
        this.retryItemDao = retryItemDao;
        this.jerseyClient=jerseyClient;
        this.ingestionService=ingestionService;
        this.threadPoolExecutor=threadPoolExecutor;

    }

    @Override
    public void createListener(ThreadConfig threadConfig) {
        for (int i=0;i< threadConfig.getListenerCount();i++) {
            log.info("Creating thread "+i+" of name "+threadConfig.getName());
            threadPoolExecutor.execute(new ListenerThread(mangedCache, threadConfig.getChannel(), retryItemDao, jerseyClient, ingestionService));
        }

    }
}
