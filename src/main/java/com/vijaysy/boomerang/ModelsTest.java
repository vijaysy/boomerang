package com.vijaysy.boomerang;

import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by vijaysy on 02/04/16.
 */
public class ModelsTest {
    public static void main(String[] args){
        Integer[] integers = new Integer[]{1,2,3,};
        create(new RetryItem("m11","Hi1", HttpMethod.GET,"URL1",0,integers,"fURL1",HttpMethod.POST));
        create(new RetryItem("m22","Hi2", HttpMethod.PUT,"URL2",0,integers,"fURL2",HttpMethod.GET));

        List<RetryItem> retryItemList = read();
        for (RetryItem retryItem1: retryItemList)
            System.out.println(retryItem1.getfHttpMethod());

    }

    public static Integer create(RetryItem retryItem) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionWithTransaction();
            session.save(retryItem);
            HibernateUtil.commitTransaction(session);
        }catch (Exception e){
            System.out.println("Exception while storing object "+e.toString());
            HibernateUtil.rollbackTransaction(session);
        }finally {
            HibernateUtil.closeSession(session);
        }
        System.out.println("Successfully created " + retryItem.toString());
        return retryItem.getId();
    }

    public static List<RetryItem> read() {
        Session session = HibernateUtil.getSession();
        @SuppressWarnings("unchecked")
        List<RetryItem> retryItems = session.createQuery("FROM RetryItem").list();
        session.close();
        System.out.println("Found " + retryItems.size() + " RetryItem");
        return retryItems;

    }
}
