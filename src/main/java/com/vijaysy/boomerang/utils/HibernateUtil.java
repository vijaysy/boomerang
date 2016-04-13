package com.vijaysy.boomerang.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Objects;

/**
 * Created by vijaysy on 11/04/16.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;



    public static Session getSession(SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        return session;
    }

    public static void closeSession(Session session){
        if(Objects.nonNull(session))
            session.close();
    }

    public static Session getSessionWithTransaction(SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }

    public static void commitTransaction(Session session) {
        session.getTransaction().commit();
    }

    public static void rollbackTransaction(Session session) {
        final Transaction txn = session.getTransaction();
        if (Objects.nonNull(txn)) {
            txn.rollback();
        }
    }



}

