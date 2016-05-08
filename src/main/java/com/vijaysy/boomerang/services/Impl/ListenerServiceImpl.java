package com.vijaysy.boomerang.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.core.restclient.RestClient;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.helpers.ListenerThread;
import com.vijaysy.boomerang.models.config.ThreadConfig;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.services.ListenerService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 11/04/16.
 */
@Slf4j
@Singleton
public class ListenerServiceImpl implements ListenerService {


    private final RetryItemDao retryItemDao;
    private final MangedCache mangedCache;
    private final IngestionService ingestionService;
    private final ThreadPoolExecutor threadPoolExecutor;

    private final HashMap<HttpMethod,RestClient> httpMethodRestClientHashMap;
    private final ObjectMapper objectMapper;

    @Inject
    public ListenerServiceImpl(RetryItemDao retryItemDao, MangedCache mangedCache, IngestionService ingestionService, ThreadPoolExecutor threadPoolExecutor,HashMap<HttpMethod,RestClient> httpMethodRestClientHashMap,ObjectMapper objectMapper){
        this.mangedCache = mangedCache;
        this.retryItemDao = retryItemDao;
        this.ingestionService=ingestionService;
        this.threadPoolExecutor=threadPoolExecutor;
        this.objectMapper=objectMapper;
        this.httpMethodRestClientHashMap=httpMethodRestClientHashMap;

    }

    @Override
    public void createListener(ThreadConfig threadConfig) {
        for (int i=0;i< threadConfig.getListenerCount();i++) {
            log.info("Creating thread "+i+" of name "+threadConfig.getName());
            threadPoolExecutor.execute(new ListenerThread(mangedCache, threadConfig.getChannel(), retryItemDao,ingestionService,httpMethodRestClientHashMap,objectMapper));
        }

    }
}
