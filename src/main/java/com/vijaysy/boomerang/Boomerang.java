package com.vijaysy.boomerang;

import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.exception.InvalidRetryItem;
import com.vijaysy.boomerang.exception.RetryCountException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

/**
 * Created by vijay.yala on 01/04/16.
 */
public class Boomerang {

    public static boolean reappear (RetryItem r) throws InvalidRetryItem,DBException,RetryCountException{

        if(Objects.isNull(r))
            throw new InvalidRetryItem("RetryItem is null");

        RetryItem dBRetryItem=readRetryItem(r.getMessageId());
        RetryItem retryItem = Objects.nonNull(dBRetryItem)? dBRetryItem: r ;

        if(retryItem.getMaxRetry()<retryItem.getNextRetry()+1)
            throw new RetryCountException("Retry count crossed the max retry count");

        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Jedis jedis = pool.getResource();
        jedis.set("RT"+retryItem.getMessageId(),"dummyValue");
        String nextRetry = retryItem.getRetryPattern()[retryItem.getNextRetry()];
        retryItem.setNextRetry(retryItem.getNextRetry()+1);
        Integer s = Integer.valueOf(nextRetry);
        jedis.expire("RT"+retryItem.getMessageId(),s*60);
        Session session = null;
        try {
            session = HibernateUtil.getSessionWithTransaction();
            session.saveOrUpdate(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            System.out.println("Exception while storing object "+e.toString());
            HibernateUtil.rollbackTransaction(session);
            jedis.del("RT"+retryItem.getMessageId());
            throw new DBException("Error occurred while storing  RetryItem");
        }finally {
            HibernateUtil.closeSession(session);
        }
        return true;
    }


    public static RetryItem isFirstRetry(String messageId){
        return readRetryItem(messageId);
    }

    public static RetryItem readRetryItem(String messageId){
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(RetryItem.class)
                .add(Restrictions.eq("messageId",messageId));
        RetryItem dBRetryItem=(RetryItem)criteria.uniqueResult();
        HibernateUtil.closeSession(session);
        return dBRetryItem;
    }
}
