package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.DaoTable;
import com.sun.corba.se.impl.javax.rmi.PortableRemoteObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

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

    protected Class<T> getGenericClass() {
        return ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).
                getActualTypeArguments()[0]);
    }

    protected String getGenericName() {
        return getGenericClass().getSimpleName();
    }

    protected Serializable save(com.company.restaurant.model.JobPosition object) {
        return getCurrentSession().save(object);
    }

    protected T findObjectByFieldCondition(String fieldName, Object value) {
        String entityName = getGenericName();
        String aliasName = entityName.substring(0,1);

        String entityAliasName = String.format("%s %s", entityName, aliasName);
        String fullFieldName = String.format("%s.%s", aliasName, fieldName);
        String valuePattern = String.format(":%s", fieldName);
        String queryExpression = fieldEntityQueryCondition(fullFieldName, valuePattern, aliasName, entityAliasName);

        Query<T> query = getCurrentSession().createQuery(queryExpression, getGenericClass());
        query.setParameter(fieldName, value);

        return query.uniqueResult();
    }
}
