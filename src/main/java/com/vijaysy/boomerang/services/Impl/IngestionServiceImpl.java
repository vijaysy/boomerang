package com.vijaysy.boomerang.services.impl;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.exception.RetryCountException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import redis.clients.jedis.Jedis;

/**
 * Created by vijaysy on 08/04/16.
 */
public class IngestionServiceImpl implements IngestionService {

    private Jedis jedis;
    private RetryItemDao retryItemDao;

    @Inject
    IngestionServiceImpl(Cache cache, RetryItemDao retryItemDao){
        this.jedis=cache.getJedisResource();
        this.retryItemDao = retryItemDao;

    }
    @Override
    public void process(RetryItem retryItem) throws Exception {
        if(retryItem.getMaxRetry()<retryItem.getNextRetry()+1)
            throw new RetryCountException("Retry count crossed the max retry count");
        int timeout=Integer.valueOf(retryItem.getRetryPattern()[retryItem.getNextRetry()])*60;
        String key = retryItem.getChannel()+"."+retryItem.getMessageId();
        retryItem.setNextRetry(retryItem.getNextRetry()+1);
        jedis.setex(key,timeout,key);
        retryItemDao.save(retryItem);

    }

    @Override
    public RetryItem getRetryItem(String messageId) {
        return retryItemDao.get(messageId);
    }
}
