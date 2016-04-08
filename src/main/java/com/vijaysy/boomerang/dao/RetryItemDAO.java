package com.vijaysy.boomerang.dao;

import com.google.inject.Inject;
import com.vijaysy.boomerang.models.RetryItem;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by vijaysy on 08/04/16.
 */
public class RetryItemDAO extends AbstractDAO<RetryItemDAO> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public RetryItemDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdate(RetryItem retryItem){
        currentSession().saveOrUpdate(retryItem);
    }
}
