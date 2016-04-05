package com.vijaysy.boomerang;

import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.exception.InvalidRetryItem;
import com.vijaysy.boomerang.exception.RetryCountException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by vijaysy on 01/04/16.
 */
@Slf4j
public final class Boomerang {

    private Boomerang(){}

    public static boolean reappear (RetryItem retryItem) throws InvalidRetryItem,DBException,RetryCountException{
        if(retryItem.getMaxRetry()<retryItem.getNextRetry()+1)
            throw new RetryCountException("Retry count crossed the max retry count");
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Jedis jedis = pool.getResource();
        jedis.set(retryItem.getChannel()+"."+retryItem.getMessageId(),"dummyValue");
        String nextRetry = retryItem.getRetryPattern()[retryItem.getNextRetry()];
        retryItem.setNextRetry(retryItem.getNextRetry()+1);
        jedis.expire(retryItem.getChannel()+"."+retryItem.getMessageId(),Integer.valueOf(nextRetry)*60);
        Session session = null;
        try {
            session = HibernateUtil.getSessionWithTransaction();
            session.saveOrUpdate(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            log.info("Exception while storing object "+e.toString());
            HibernateUtil.rollbackTransaction(session);
            jedis.del(retryItem.getChannel()+"."+retryItem.getMessageId());
            throw new DBException("Error occurred while storing  RetryItem");
        }finally {
            HibernateUtil.closeSession(session);
            jedis.close();

        }
        return true;
    }


    public static Optional<RetryItem> isRetryExist(String messageId){
        RetryItem retryItem = readRetryItem(messageId);
        return Objects.nonNull(retryItem)?Optional.of(retryItem):Optional.<RetryItem>empty();
    }

    public static RetryItem readRetryItem(String messageId){
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(RetryItem.class)
                .add(Restrictions.eq("messageId",messageId));
        RetryItem dBRetryItem=(RetryItem)criteria.uniqueResult();
        HibernateUtil.closeSession(session);
        return dBRetryItem;
    }

    public static boolean isValidRetryItem(RetryItem retryItem){
        //TODO: need to validate maxRetry for -ve values and null attributes
        return Objects.isNull(retryItem);
    }
}
