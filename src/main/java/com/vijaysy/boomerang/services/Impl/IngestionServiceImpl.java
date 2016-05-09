package com.vijaysy.boomerang.services.impl;

import com.google.inject.Inject;
import com.vijaysy.boomerang.core.MangedCache;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by vijaysy on 08/04/16.
 */
@Slf4j
public class IngestionServiceImpl implements IngestionService {

    private final RetryItemDao retryItemDao;
    private final MangedCache mangedCache;

    @Inject
    IngestionServiceImpl(MangedCache mangedCache, RetryItemDao retryItemDao){
        this.mangedCache = mangedCache;
        this.retryItemDao = retryItemDao;

    }
    @Override
    public boolean process(RetryItem retryItem)  {
        try (Jedis jedis = mangedCache.getJedisResource()) {
            int timeout = Integer.valueOf(retryItem.getRetryPattern()[retryItem.getNextRetry()]) * 60;
            String key = retryItem.getChannel() + "." + retryItem.getMessageId();
            retryItem.setNextRetry(retryItem.getNextRetry() + 1);
            jedis.setex(key, timeout, key);
            retryItemDao.save(retryItem);
            return true;
        } catch (Exception e){
            log.error("Exception while processing, messageID {} \n {}",retryItem.getMessageId(),e);
            return false;
        }

    }

    @Override
    public boolean againProcess(RetryItem retryItem) {
        try (Jedis jedis = mangedCache.getJedisResource()) {
            int timeout = Integer.valueOf(retryItem.getRetryPattern()[retryItem.getNextRetry()]) * 60;
            String key = retryItem.getChannel() + "." + retryItem.getMessageId();
            retryItem.setNextRetry(retryItem.getNextRetry() + 1);
            jedis.setex(key, timeout, key);
            retryItemDao.update(retryItem);
            return true;
        } catch (Exception e){
            log.error("Exception while processing, messageID {} \n {}",retryItem.getMessageId(),e);
            return false;
        }
    }

    @Override
    public Set<String> getKeys(String pattern) {
        try (Jedis jedis = mangedCache.getJedisResource()){
            return (pattern!=null&& !pattern.isEmpty()) ? jedis.keys(pattern+"*") :jedis.keys("*");
        }
    }

    @Override
    public Optional<RetryItem> getRetryItem(String messageId) throws DBException {
        RetryItem retryItem = retryItemDao.get(messageId);
        return (Objects.isNull(retryItem))?Optional.<RetryItem>empty():Optional.of(retryItem);
    }



}
