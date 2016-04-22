package com.vijaysy.boomerang.services;

import com.vijaysy.boomerang.models.RetryItem;

import java.util.Optional;

/**
 * Created by vijaysy on 08/04/16.
 */
public interface IngestionService {
    boolean process(RetryItem retryItem);

    Optional<RetryItem> getRetryItem(String messageId);

    boolean againProcess(RetryItem retryItem);
}
