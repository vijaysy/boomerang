package com.vijaysy.boomerang.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by vijaysy on 08/04/16.
 */
@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class Reappear {

    private final IngestionService ingestionService;

    @Inject
    public Reappear(IngestionService ingestionService){
        this.ingestionService=ingestionService;
    }
    @POST()
    @ExceptionMetered
    @Timed
    @Path("reappear")
    public void doReappear(RetryItem retryItem)throws Exception{
        log.info("Retry Item received: "+retryItem);
        ingestionService.process(retryItem);

    }
}
