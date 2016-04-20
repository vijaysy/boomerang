package com.vijaysy.boomerang.services.impl;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.Cache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Created by vijaysy on 08/04/16.
 */
@Slf4j
public class IngestionServiceImpl implements IngestionService {

    private final RetryItemDao retryItemDao;
    private final Cache cache;

    @Inject
    IngestionServiceImpl(Cache cache, RetryItemDao retryItemDao){
        this.cache=cache;
        this.retryItemDao = retryItemDao;

    }
    @Override
    public boolean process(RetryItem retryItem)  {
        try (Jedis jedis = cache.getJedisResource()) {
            int timeout = Integer.valueOf(retryItem.getRetryPattern()[retryItem.getNextRetry()]) * 60;
            String key = retryItem.getChannel() + "." + retryItem.getMessageId();
            retryItem.setNextRetry(retryItem.getNextRetry() + 1);
            jedis.setex(key, timeout, key);
            retryItemDao.saveOrUpdate(retryItem);
            return true;
        } catch (Exception e){
            log.info("Exception while processing, messageID "+retryItem.getMessageId()+"\n"+e.toString());
            return false;
        }

    }

    @Override
    public RetryItem getRetryItem(String messageId) {
        return retryItemDao.get(messageId);
    }
}
