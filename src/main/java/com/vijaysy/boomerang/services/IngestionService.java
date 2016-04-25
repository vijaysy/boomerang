package com.vijaysy.boomerang.services;

import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;

import java.util.Optional;
import java.util.Set;

/**
 * Created by vijaysy on 08/04/16.
 */
public interface IngestionService {
    boolean process(RetryItem retryItem);

    Optional<RetryItem> getRetryItem(String messageId) throws DBException;

    boolean againProcess(RetryItem retryItem);

    Set<String> getKeys(String pattern);
}
