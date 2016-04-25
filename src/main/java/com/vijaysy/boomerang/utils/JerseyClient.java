package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.models.RetryItem;

import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 18/04/16.
 */
public interface JerseyClient {
    Response returnExecute(RetryItem retryItem, Response response);

    Response execute(RetryItem retryItem);
}
