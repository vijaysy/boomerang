package com.vijaysy.boomerang.core;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Singleton;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 10/04/16.
 */
@Singleton
public class ThreadPool implements Managed {

    private ThreadPoolExecutor executorService;

    @Inject
    public ThreadPool(ThreadPoolExecutor executorService){
        this.executorService=executorService;
    }

    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdown();

    }

}
