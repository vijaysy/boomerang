package com.vijaysy.boomerang.services.impl;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.helpers.ListenerThread;
import com.vijaysy.boomerang.models.config.ThreadConfig;
import com.vijaysy.boomerang.services.ListenerService;
import com.vijaysy.boomerang.services.RetryItemHandler;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 11/04/16.
 */
@Slf4j
@Singleton
public class ListenerServiceImpl implements ListenerService {

    private final MangedCache mangedCache;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final RetryItemHandler retryItemHandler;

    @Inject
    public ListenerServiceImpl( MangedCache mangedCache, ThreadPoolExecutor threadPoolExecutor, RetryItemHandler retryItemHandler){
        this.mangedCache = mangedCache;
        this.threadPoolExecutor=threadPoolExecutor;
        this.retryItemHandler = retryItemHandler;
    }

    @Override
    public void createListener(ThreadConfig threadConfig) {
        for (int i=0;i< threadConfig.getListenerCount();i++) {
            log.info("Creating thread "+i+" of name "+threadConfig.getName());
            threadPoolExecutor.execute(new ListenerThread(mangedCache, threadConfig.getChannel(), retryItemHandler));
        }
    }
}
