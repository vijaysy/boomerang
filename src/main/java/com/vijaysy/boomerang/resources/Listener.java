package com.vijaysy.boomerang.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vijaysy.boomeranglistener.models.ThreadConfig;
import com.vijaysy.boomeranglistener.services.ListenerService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

    @Inject
    public Listener(ListenerService listenerService){
        this.listenerService=listenerService;

    }

    @POST()
    @ExceptionMetered
    @Timed
    @Path("listen")
    public void doListen(ThreadConfig threadConfig)throws Exception{
        log.info("Listener creation request: "+threadConfig);
        listenerService.createListener(threadConfig);
    }
}
