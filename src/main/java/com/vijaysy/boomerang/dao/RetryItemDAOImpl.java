package com.vijaysy.boomerang.dao;

import com.google.inject.Inject;
import com.vijaysy.boomerang.utils.HibernateUtil;
import com.vijaysy.boomerang.models.RetryItem;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 * Created by vijaysy on 08/04/16.
 */
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
    public RetryItem get(String messageId){
        Session session = HibernateUtil.getSession(sessionFactory);
        try {
            Criteria criteria = session.createCriteria(RetryItem.class)
                    .add(Restrictions.eq("messageId",messageId));
            return (RetryItem)criteria.uniqueResult();
        }catch (Exception e){
            throw e;
        }finally {
            HibernateUtil.closeSession(session);
        }

    }


    @Override
    public void saveOrUpdate(RetryItem retryItem){
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.saveOrUpdate(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw e;
        }finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Override
    public void save(RetryItem retryItem){
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.save(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw e;
        }finally {
            HibernateUtil.closeSession(session);
        }
    }
}
