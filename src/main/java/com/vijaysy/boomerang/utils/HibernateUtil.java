package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.models.RetryItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Objects;

/**
 * Created by vijay.yala on 02/04/16.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if(Objects.isNull(sessionFactory)){
            Configuration configuration = new Configuration().configure(new File("/Users/vijay.yala/vijay/java/boomerang/config/hibernate.cfg.xml"));
            configuration.addAnnotatedClass(RetryItem.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        }
        return sessionFactory;
    }

    public static Session getSession(){
        Session session = getSessionFactory().openSession();
        return session;
    }

    public static void closeSession(Session session){
        if(Objects.nonNull(session))
            session.close();
    }

    public static Session getSessionWithTransaction(){
        Session session = getSessionFactory().openSession();
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
