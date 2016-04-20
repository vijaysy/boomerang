package com.vijaysy.boomerang.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.models.RetryItem;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
@Singleton
public class JerseyClientImpl implements JerseyClient{
    private final Client client;
    private Response response;
    private final ObjectMapper objectMapper;

    @Inject
    public JerseyClientImpl(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response execute(RetryItem retryItem) {
        WebTarget webTarget = client.target(retryItem.getHttpUri());
        log.info("Making retry call for messageId:" + retryItem.getMessageId());
        try {
            switch (retryItem.getHttpMethod()) {
                case POST:
                    response = webTarget.request().headers(getHeaders(retryItem.getHeaders())).buildPost(Entity.json(retryItem.getMessage())).invoke();
                    break;
                case GET:
                    response = webTarget.request().headers(getHeaders(retryItem.getHeaders())).buildGet().invoke();
                    break;
                case PUT:
                    response = webTarget.request().headers(getHeaders(retryItem.getHeaders())).buildPut(Entity.json(retryItem.getMessage())).invoke();
                    break;
                default:
                    return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error("Exception while making call for messageID "+retryItem.getMessageId());
            log.error("Exception:"+e.toString());
            return Response.status(503).build();
        }
        log.info("Response for retry call" + response.toString());
        if(Response.Status.Family.SUCCESSFUL.equals(response.getStatus())&&retryItem.getNeedResponse())
             executeSuccess(retryItem,response);
        return response;

    }

    @Override
    public boolean executeFallBack(RetryItem retryItem, FallBackReasons fallBackReasons) {
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback");
         webTarget.request().header("retry","false").buildPut(Entity.text("")).invoke();
        return true;
    }

    @Override
    public Response executeSuccess(RetryItem retryItem,Response response){
        WebTarget webTarget = client.target(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback");
        return  webTarget.request().header("retry","true").buildPut(Entity.json(response)).invoke();
    }

    private MultivaluedHashMap<String,Object> getHeaders(String headers) {
        try {
            return (headers != null && !headers.isEmpty()) ? objectMapper.readValue(headers, MultivaluedHashMap.class):null;
        } catch (IOException e) {
            log.error("Exception while deserialization the headers");
            return null;

        }
    }


}
