package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.models.RetryItem;

import javax.ws.rs.core.Response;

/**
 * Created by vijaysy on 18/04/16.
 */
public interface JerseyClient {
    boolean executeFallBack(RetryItem retryItem, FallBackReasons fallBackReasons);

    Response executeSuccess(RetryItem retryItem, Response response);

    Response execute(RetryItem retryItem);
}
