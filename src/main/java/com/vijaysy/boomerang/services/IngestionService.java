package com.vijaysy.boomerang.services;

import com.vijaysy.boomerang.models.RetryItem;

/**
 * Created by vijaysy on 08/04/16.
 */
public interface IngestionService {
    boolean process(RetryItem retryItem);

    RetryItem getRetryItem(String messageId);
}
