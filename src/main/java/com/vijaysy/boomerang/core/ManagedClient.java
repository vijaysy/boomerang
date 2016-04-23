package com.vijaysy.boomerang.core;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 21/04/16.
 */
@Slf4j
@Singleton
public class ManagedClient implements Managed {

    private final Client client;

    @Inject
    public ManagedClient(Client client){
        this.client=client;
    }

    @Override
    public void start() throws Exception {

    }

    public Response invokePost(String uri, Object object, MultivaluedMap<String, Object> headers, String messageId){
        try {
            WebTarget webTarget = client.target(uri);
            return webTarget.request().headers(headers).buildPost(Entity.json(object)).invoke();
        }catch (Exception e){
            log.error("Exception while making call for messageID "+messageId);
            log.error("Exception:"+e.toString());
            return Response.status(503).build();
        }
    }

    public Response invokePut(String uri,Object object,MultivaluedMap<String, Object> headers,String messageId){
        try {
            WebTarget webTarget = client.target(uri);
            return webTarget.request().headers(headers).buildPut(Entity.json(object)).invoke();
        }catch (Exception e){
            log.error("Exception while making call for messageID "+messageId);
            log.error("Exception:"+e.toString());
            return Response.status(503).build();
        }
    }

    public Response invokeGet(String uri,MultivaluedMap<String, Object> headers,String messageId){
        try {
            WebTarget webTarget = client.target(uri);
            return webTarget.request().headers(headers).buildGet().invoke();
        }catch (Exception e){
            log.error("Exception while making call for messageID "+messageId);
            log.error("Exception:"+e.toString());
            return Response.status(503).build();
        }
    }

    @Override
    public void stop() throws Exception {
        log.info("Closing Jersey client ");
        client.close();

    }
}
