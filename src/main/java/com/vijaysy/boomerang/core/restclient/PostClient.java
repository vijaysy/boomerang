package com.vijaysy.boomerang.core.restclient;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 09/05/16.
 */
@Slf4j
public class PostClient implements RestClient {

    private final Client client;

    @Inject
    public PostClient(Client client) {
        this.client=client;
    }

    @Override
    public Response invoke(String uri, Object object, MultivaluedMap<String, Object> headers, String messageId) {
        try {
            WebTarget webTarget = client.target(uri);
            return webTarget.request().headers(headers).buildPost(Entity.json(object)).invoke();
        }catch (Exception e){
            log.error("Exception while making call for messageID "+messageId);
            log.error("Exception:"+e.toString());
            return Response.status(503).build();
        }
    }
}
