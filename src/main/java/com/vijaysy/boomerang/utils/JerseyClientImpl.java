package com.vijaysy.boomerang.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.core.ManagedClient;
import com.vijaysy.boomerang.models.RetryItem;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by vijaysy on 02/04/16.
 */
@Slf4j
@Singleton
public class JerseyClientImpl implements JerseyClient {
    private final ObjectMapper objectMapper;
    private final ManagedClient managedClient;

    @Inject
    public JerseyClientImpl(ObjectMapper objectMapper, ManagedClient managedClient) {
        this.objectMapper = objectMapper;
        this.managedClient = managedClient;
    }

    @Override
    public Response execute(RetryItem retryItem) {
        log.info("Making retry call for messageId:" + retryItem.getMessageId());
        switch (retryItem.getHttpMethod()) {
            case POST:
                return managedClient.invokePost(retryItem.getHttpUri(), retryItem.getMessage(), getHeaders(retryItem.getHeaders()), retryItem.getMessageId());
            case GET:
                return managedClient.invokeGet(retryItem.getHttpUri(), getHeaders(retryItem.getHeaders()), retryItem.getMessageId());
            case PUT:
                return managedClient.invokePut(retryItem.getHttpUri(), retryItem.getMessage(), getHeaders(retryItem.getHeaders()), retryItem.getMessageId());
            default:
                return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @Override
    public Response returnExecute(RetryItem retryItem, Response response, boolean flg) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("retry", flg);
        headers.putSingle("reason",retryItem.getFallBackReasons());
        Object body= (retryItem.getNeedResponse())?response:"";
        return  managedClient.invokePut(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback",body,headers,"executeFallBack");
    }

    private MultivaluedHashMap<String, Object> getHeaders(String headers) {
        try {
            return (headers != null && !headers.isEmpty()) ? objectMapper.readValue(headers, MultivaluedHashMap.class) : null;
        } catch (IOException e) {
            log.error("Exception while deserialization the headers");
            return null;

        }
    }


}
