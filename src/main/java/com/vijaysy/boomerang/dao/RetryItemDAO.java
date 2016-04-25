package com.vijaysy.boomerang.dao;

import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;

import java.util.List;

/**
 * Created by vijaysy on 19/04/16.
 */
public interface RetryItemDao {
    RetryItem get(String messageId) throws DBException;

    void saveOrUpdate(RetryItem retryItem) throws DBException;

    void save(RetryItem retryItem) throws DBException;

    void update(RetryItem retryItem) throws DBException;

    List<RetryItem> getAll() throws DBException;
}
