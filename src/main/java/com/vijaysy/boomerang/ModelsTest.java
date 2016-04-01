package com.vijaysy.boomerang;

import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.List;

/**
 * Created by vijay.yala on 02/04/16.
 */
public class ModelsTest {
    public static SessionFactory sessionFactory= getSessionFactory();
    public static void main(String[] args){
        Integer[] integers = new Integer[]{1,2,3,};
        create(new RetryItem("Hi1", HttpMethod.GET,"URL1",0,integers,"fURL1",HttpMethod.POST));
        create(new RetryItem("Hi2", HttpMethod.PUT,"URL2",0,integers,"fURL2",HttpMethod.GET));

        List<RetryItem> retryItemList = read();
        for (RetryItem retryItem1: retryItemList)
            System.out.println(retryItem1.getfHttpMethod());

    }

    public static SessionFactory getSessionFactory() {
        File file = new File("/Users/vijay.yala/vijay/java/boomerang/config/hibernate.cfg.xml");
        Configuration configuration = new Configuration().configure(file);
        configuration.addAnnotatedClass(RetryItem.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        return sessionFactory;
    }

    public static Integer create(RetryItem e) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getId();
    }

    public static List<RetryItem> read() {
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<RetryItem> retryItems = session.createQuery("FROM RetryItem").list();
        session.close();
        System.out.println("Found " + retryItems.size() + " RetryItem");
        return retryItems;

    }
}
