package com.vijaysy.boomerang.core;

import com.google.inject.Inject;
import com.vijaysy.boomerang.BoomerangConfiguration;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.utils.JerseyClient;
import com.vijaysy.boomerang.utils.ListenerThread;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 20/04/16.
 */
@Singleton
@Slf4j
public class ManagedListener implements Managed {

    private final BoomerangConfiguration boomerangConfiguration;
    private final JerseyClient jerseyClient;
    private final RetryItemDao retryItemDao;
    private final MangedCache mangedCache;
    private final IngestionService ingestionService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public ManagedListener(BoomerangConfiguration boomerangConfiguration, ThreadPoolExecutor threadPoolExecutor, JerseyClient jerseyClient , RetryItemDao retryItemDao, MangedCache mangedCache, IngestionService ingestionService){
        this.boomerangConfiguration=boomerangConfiguration;
        this.threadPoolExecutor=threadPoolExecutor;
        this.jerseyClient=jerseyClient;
        this.retryItemDao=retryItemDao;
        this.mangedCache = mangedCache;
        this.ingestionService=ingestionService;
    }
    @Override
    public void start() throws Exception {
        boomerangConfiguration.getThreadConfigs().forEach(threadConfig -> {
            for (int i=0;i< threadConfig.getListenerCount();i++) {
                log.info("Creating thread "+i+" of name "+threadConfig.getName());
                threadPoolExecutor.execute(new ListenerThread(mangedCache, threadConfig.getChannel(), retryItemDao, jerseyClient, ingestionService));
            }
        });

    }

    @Override
    public void stop() throws Exception {
        log.info("Shutting down thread pool executor ");
        threadPoolExecutor.shutdown();

    }
}
