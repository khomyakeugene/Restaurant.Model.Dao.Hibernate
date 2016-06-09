package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.proto.DaoTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoTable<T> extends DaoTable {
    private SessionFactory sessionFactory;

    public HDaoTable() {
        initMetadata();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
