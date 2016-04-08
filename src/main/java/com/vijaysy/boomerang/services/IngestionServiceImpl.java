package com.vijaysy.boomerang.services;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemDAO;
import com.vijaysy.boomerang.exception.RetryCountException;
import com.vijaysy.boomerang.models.RetryItem;
import redis.clients.jedis.Jedis;

/**
 * Created by vijaysy on 08/04/16.
 */
public class IngestionServiceImpl implements IngestionService {

    private Jedis jedis;
    private RetryItemDAO retryItemDAO;

    @Inject
    IngestionServiceImpl(Cache cache, RetryItemDAO retryItemDAO){
        this.jedis=cache.getJedisResource();
        this.retryItemDAO=retryItemDAO;

    }
    @Override
    public void process(RetryItem retryItem) throws Exception {
        if(retryItem.getMaxRetry()<retryItem.getNextRetry()+1)
            throw new RetryCountException("Retry count crossed the max retry count");
        int timeout=Integer.valueOf(retryItem.getRetryPattern()[retryItem.getNextRetry()])*60;
        String key = retryItem.getChannel()+"."+retryItem.getMessageId();
        retryItem.setNextRetry(retryItem.getNextRetry()+1);
        jedis.setex(key,timeout,key);
        retryItemDAO.saveOrUpdate(retryItem);

    }
}
