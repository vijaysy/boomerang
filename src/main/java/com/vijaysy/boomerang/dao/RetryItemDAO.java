package com.vijaysy.boomerang.dao;

import com.vijaysy.boomerang.models.RetryItem;

/**
 * Created by vijaysy on 19/04/16.
 */
public interface RetryItemDao {
    RetryItem get(String messageId);

    void saveOrUpdate(RetryItem retryItem);

    void save(RetryItem retryItem);

    void update(RetryItem retryItem);
}
