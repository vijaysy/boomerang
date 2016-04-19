package com.vijaysy.boomerang.services;

import com.vijaysy.boomerang.models.RetryItem;

/**
 * Created by vijaysy on 08/04/16.
 */
public interface IngestionService {
    void process(RetryItem retryItem) throws Exception;
    RetryItem getRetryItem(String messageId);
}
