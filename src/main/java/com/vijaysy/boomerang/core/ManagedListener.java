package com.vijaysy.boomerang.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.BoomerangConfiguration;
import com.vijaysy.boomerang.core.restclient.RestClient;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.helpers.ListenerThread;
import com.vijaysy.boomerang.services.IngestionService;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 20/04/16.
 */
@Singleton
@Slf4j
public class ManagedListener implements Managed {

    private final BoomerangConfiguration boomerangConfiguration;
    private final RetryItemDao retryItemDao;
    private final MangedCache mangedCache;
    private final IngestionService ingestionService;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final HashMap<HttpMethod,RestClient> httpMethodRestClientHashMap;
    private final ObjectMapper objectMapper;

    @Inject
    public ManagedListener(BoomerangConfiguration boomerangConfiguration, ThreadPoolExecutor threadPoolExecutor, RetryItemDao retryItemDao, MangedCache mangedCache, IngestionService ingestionService,HashMap<HttpMethod,RestClient> httpMethodRestClientHashMap,ObjectMapper objectMapper){
        this.boomerangConfiguration=boomerangConfiguration;
        this.threadPoolExecutor=threadPoolExecutor;
        this.retryItemDao=retryItemDao;
        this.mangedCache = mangedCache;
        this.ingestionService=ingestionService;
        this.objectMapper=objectMapper;
        this.httpMethodRestClientHashMap=httpMethodRestClientHashMap;
    }
    @Override
    public void start() throws Exception {
        boomerangConfiguration.getThreadConfigs().forEach(threadConfig -> {
            for (int i=0;i< threadConfig.getListenerCount();i++) {
                log.info("Creating thread "+i+" of name "+threadConfig.getName());
                threadPoolExecutor.execute(new ListenerThread(mangedCache, threadConfig.getChannel(), retryItemDao, ingestionService,httpMethodRestClientHashMap,objectMapper));
            }
        });

    }

    @Override
    public void stop() throws Exception {
        log.info("Shutting down thread pool executor ");
        threadPoolExecutor.shutdownNow();

    }
}
