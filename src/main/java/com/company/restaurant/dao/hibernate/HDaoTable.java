package com.company.restaurant.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoTable<T> {
    private SessionFactory sessionFactory;

    protected String tableName;
    protected String viewName;
    protected String orderByCondition;

    protected abstract void initMetadata();

    public HDaoTable() {
        initMetadata();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSeccion() {
        return sessionFactory.getCurrentSession();
    }
}
