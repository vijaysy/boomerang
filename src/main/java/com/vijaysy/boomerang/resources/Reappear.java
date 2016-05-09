package com.vijaysy.boomerang.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.exception.ReappearException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.Set;

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
    public Reappear(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @POST()
    @ExceptionMetered
    @Timed
    @Path("reappear")
    public boolean doReappear(RetryItem retryItem) throws Exception {
        log.info("Retry Item received: {}",retryItem);
        if (ingestionService.process(retryItem))
            return true;
        throw new ReappearException("Exception happened, please check the log with messageID:" + retryItem.getMessageId());

    }

    @GET
    @ExceptionMetered
    @Timed
    @Path("get")
    public Optional<RetryItem> getRetryItem(@QueryParam("messageId") String messageId) throws DBException {
        return ingestionService.getRetryItem(messageId);
    }


    @GET
    @ExceptionMetered
    @Timed
    @Path("keys")
    public Set<String> getKeys(@QueryParam("pattern") String pattern) {
        return ingestionService.getKeys(pattern);

    }
}
