package com.vijaysy.boomerang.dao;

import com.google.inject.Inject;
import com.vijaysy.boomeranglistener.Utils.HibernateUtil;
import com.vijaysy.boomeranglistener.models.RetryItem;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 * Created by vijaysy on 08/04/16.
 */
public class RetryItemListenerDAO extends AbstractDAO<RetryItemListenerDAO> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    private SessionFactory sessionFactory;
    @Inject
    public RetryItemListenerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory=sessionFactory;
    }

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


    public void saveOrUpdate(RetryItem retryItem){
        Session session = HibernateUtil.getSessionWithTransaction(sessionFactory);
        try {
            session.saveOrUpdate(retryItem);

        }catch (Exception e){
            HibernateUtil.rollbackTransaction(session);
            throw e;

        }finally {
            HibernateUtil.closeSession(session);
        }

    }
}
