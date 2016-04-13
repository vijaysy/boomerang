package com.vijaysy.boomerang.utils;


import com.google.inject.Inject;
import com.vijaysy.boomeranglistener.models.RetryItem;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
public class JerseyClient {
    private Client client;
    private Response response;

    @Inject
    public JerseyClient(Client client){
        this.client= client;
    }

    public Response execute(RetryItem retryItem) {
        WebTarget webTarget = client.target(retryItem.getHttpUri());
        log.info("Making retry call for messageId:"+retryItem.getMessageId());
        switch (retryItem.getHttpMethod()){
            case POST: response=webTarget.request().buildPost(Entity.json(retryItem.getMessage())).invoke();
                break;
            case GET: response=webTarget.request().buildGet().invoke();
                break;
            case PUT: response=webTarget.request().buildPut(Entity.json(retryItem.getMessage())).invoke();
                break;
            default: return Response.status(Response.Status.BAD_REQUEST).build();
        }
        log.info("Response for retry call"+response.toString());
      return response;

    }

    public Response executeFallBack(RetryItem retryItem){
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri()+"/"+retryItem.getMessageId()+"/fallback");
        response=webTarget.request().buildPut(Entity.text("")).invoke();
        return response;
    }


}
