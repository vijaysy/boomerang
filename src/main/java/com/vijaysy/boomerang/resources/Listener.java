package com.vijaysy.boomerang.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.models.config.ThreadConfig;
import com.vijaysy.boomerang.models.PoolStatus;
import com.vijaysy.boomerang.services.ListenerService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vijaysy on 10/04/16.
 */
@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class Listener {
    private final ListenerService listenerService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public Listener(ListenerService listenerService,ThreadPoolExecutor threadPoolExecutor){
        this.listenerService=listenerService;
        this.threadPoolExecutor=threadPoolExecutor;

    }

    @POST()
    @ExceptionMetered
    @Timed
    @Path("listen")
    public void doListen(ThreadConfig threadConfig)throws Exception{
        log.info("Listener creation request: "+threadConfig);
        listenerService.createListener(threadConfig);
    }

    @GET
    @Timed
    @Path("status")
    public PoolStatus getStatus(){
        return new PoolStatus(threadPoolExecutor.getActiveCount(),threadPoolExecutor.getCorePoolSize(),threadPoolExecutor.getPoolSize(),threadPoolExecutor.getTaskCount());
    }

}
