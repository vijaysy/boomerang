package com.vijaysy.boomerang.dao;

import com.google.inject.Inject;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.helpers.HibernateHelper;
import com.vijaysy.boomerang.models.RetryItem;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by vijaysy on 08/04/16.
 */
@Slf4j
public class RetryItemDaoImpl extends AbstractDAO<RetryItemDao> implements RetryItemDao {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    private SessionFactory sessionFactory;
    @Inject
    public RetryItemDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory=sessionFactory;
    }

    @Override
    public RetryItem get(String messageId) throws DBException {
        Session session = HibernateHelper.getSession(sessionFactory);
        try {
            Criteria criteria = session.createCriteria(RetryItem.class)
                    .add(Restrictions.eq("messageId",messageId));
            return (RetryItem)criteria.uniqueResult();
        }catch (Exception e){
            log.error("Exception while getting retryItem with messageId: {} ",messageId);
            log.error("Exception: {}",e);
            throw new DBException("Exception while getting retryItem with messageId: "+messageId,e);
        }finally {
            HibernateHelper.closeSession(session);
        }

    }


    @Override
    public void saveOrUpdate(RetryItem retryItem) throws DBException {
        Session session = HibernateHelper.getSessionWithTransaction(sessionFactory);
        try {
            session.saveOrUpdate(retryItem);
            HibernateHelper.commitTransaction(session);
        }catch (Exception e){
            HibernateHelper.rollbackTransaction(session);
            throw new DBException("Exception while saveOrUpdate retryItem with messageId: "+retryItem.getMessageId(),e);
        }finally {
            HibernateHelper.closeSession(session);
        }
    }

    @Override
    public void save(RetryItem retryItem) throws DBException {
        Session session = HibernateHelper.getSessionWithTransaction(sessionFactory);
        try {
            session.save(retryItem);
            HibernateHelper.commitTransaction(session);
        }catch (Exception e){
            HibernateHelper.rollbackTransaction(session);
            throw new DBException("Exception while saving retryItem with messageId: "+retryItem.getMessageId(),e);
        }finally {
            HibernateHelper.closeSession(session);
        }
    }

    @Override
    public void update(RetryItem retryItem) throws DBException {
        Session session = HibernateHelper.getSessionWithTransaction(sessionFactory);
        try {
            session.update(retryItem);
            HibernateHelper.commitTransaction(session);
        }catch (Exception e){
            HibernateHelper.rollbackTransaction(session);
            throw new DBException("Exception while updating with messageId:"+retryItem.getMessageId(),e);
        }finally {
            HibernateHelper.closeSession(session);
        }

    }

    @Override
    public List<RetryItem> getAll() throws DBException {
        Session session = HibernateHelper.getSession(sessionFactory);
        try {
            return session.createCriteria(RetryItem.class).list();
        }catch (Exception e){
            throw new DBException("Exception while reading all retryItems",e);
        }finally {
            HibernateHelper.closeSession(session);
        }

    }


}
