package com.vijaysy.boomerang.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.models.RetryItem;
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
    private final Client client;
    private Response response;
    private final ObjectMapper objectMapper;

    @Inject
    public JerseyClient(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public Response execute(RetryItem retryItem) {
        WebTarget webTarget = client.target(retryItem.getHttpUri());
        log.info("Making retry call for messageId:" + retryItem.getMessageId());
        try {
            switch (retryItem.getHttpMethod()) {
                case POST:
                    response = webTarget.request().buildPost(Entity.json(retryItem.getMessage())).invoke();
                    break;
                case GET:
                    response = webTarget.request().buildGet().invoke();
                    break;
                case PUT:
                    response = webTarget.request().buildPut(Entity.json(retryItem.getMessage())).invoke();
                    break;
                default:
                    return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.info("Exception while making call for messageID "+retryItem.getMessageId() +e.toString());
            return null;
        }
        log.info("Response for retry call" + response.toString());
        if(Response.Status.Family.SUCCESSFUL.equals(response.getStatus())&&retryItem.getNeedResponse())
             executeSuccess(retryItem,response);
        return response;

    }

    public Response executeFallBack(RetryItem retryItem) {
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback");
        return webTarget.request().header("retry","false").buildPut(Entity.text("")).invoke();
    }

    public Response executeSuccess(RetryItem retryItem,Response response){
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback");
        return  webTarget.request().header("retry","true").buildPut(Entity.json(response)).invoke();
    }


}
