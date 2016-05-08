package com.vijaysy.boomerang.core.restclient;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 09/05/16.
 */
public interface RestClient {
    Response invoke (String uri, Object object, MultivaluedMap<String, Object> headers, String messageId);
}
