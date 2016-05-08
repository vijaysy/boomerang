package com.vijaysy.boomerang.dao;

import com.google.inject.Inject;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.helpers.HibernateUtil;
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
        Session session = HibernateUtil.getSession(sessionFactory);
        try {
            Criteria criteria = session.createCriteria(RetryItem.class)
                    .add(Restrictions.eq("messageId",messageId));
            return (RetryItem)criteria.uniqueResult();
        }catch (Exception e){
            log.error("Exception while getting retryItem with messageId: "+messageId);
            log.error("Exception: "+e.toString());
            throw new DBException("Exception while getting retryItem with messageId: "+messageId,e);
        }finally {
            HibernateUtil.closeSession(session);
        }

    }


    @Override
    public void saveOrUpdate(RetryItem retryItem) throws DBException {
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.saveOrUpdate(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw new DBException("Exception while saveOrUpdate retryItem with messageId: "+retryItem.getMessageId(),e);
        }finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Override
    public void save(RetryItem retryItem) throws DBException {
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.save(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw new DBException("Exception while saving retryItem with messageId: "+retryItem.getMessageId(),e);
        }finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Override
    public void update(RetryItem retryItem) throws DBException {
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.update(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw new DBException("Exception while updating with messageId: "+retryItem.getMessageId(),e);
        }finally {
            HibernateUtil.closeSession(session);
        }

    }

    @Override
    public List<RetryItem> getAll() throws DBException {
        Session session = HibernateUtil.getSession(sessionFactory);
        try {
            return session.createCriteria(RetryItem.class).list();
        }catch (Exception e){
            throw new DBException("Exception while reading all retryItems",e);
        }finally {
            HibernateUtil.closeSession(session);
        }

    }


}
