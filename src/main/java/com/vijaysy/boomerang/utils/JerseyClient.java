package com.vijaysy.boomerang.utils;


import com.vijaysy.boomerang.models.RetryItem;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
public class JerseyClient {
    private Client client;
    private RetryItem retryItem;
    private Response response;

    public JerseyClient(RetryItem retryItem){
        this.retryItem=retryItem;
        this.client= ClientBuilder.newClient(); // todo don't create every time ... use connection pool
    }

    //TODO: need to check overload in creation of WebTarget and Client

    public boolean execute() {
        WebTarget webTarget = client.target(retryItem.getHttpUri());
        log.info("Making retry call for messageId:"+retryItem.getMessageId());
        switch (retryItem.getHttpMethod()){
            case POST: response=webTarget.request().buildPost(Entity.json(retryItem.getMessage())).invoke();
                break;
            case GET: response=webTarget.request().buildGet().invoke();
                break;
            case PUT: response=webTarget.request().buildPut(Entity.json(retryItem.getMessage())).invoke();
                break;
            default: return false;
        }
        log.info("Response for retry call"+response.toString());
      return true;

    }

    public boolean executeFallBack(){
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri()+"/"+retryItem.getMessageId()+"/fallback");
        response=webTarget.request().buildPut(Entity.text("")).invoke();
        return true;
    }


}
