package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.SqlExpressions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoEntity<T> {
    private static final String DEFAULT_ORDER_BY_CONDITION_PATTERN = "ORDER BY %s";

    protected String nameAttributeName;
    protected String orderByCondition;

    private SessionFactory sessionFactory;

    public HDaoEntity() {
        initMetadata();
    }

    protected abstract void initMetadata();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private Class<T> getGenericClass() {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0]);
    }

    private String getGenericName() {
        return getGenericClass().getSimpleName();
    }

    protected String getEntityName() {
        return getGenericName();
    }

    protected String getOrderByCondition(String attributeName) {
        if (orderByCondition == null) {
            orderByCondition = String.format(DEFAULT_ORDER_BY_CONDITION_PATTERN, attributeName);
        }

        return orderByCondition;
    }

    protected String getDefaultOrderByCondition() {
        if (orderByCondition == null) {
            orderByCondition = getOrderByCondition(getEntityIdAttributeName());
        }

        return orderByCondition;
    }

    private EntityType<T> getEntityType() {
        return sessionFactory.getMetamodel().entity(getGenericClass());
    }

    private String getEntityIdAttributeName() {
        String result = null;

        EntityType<T> entityType = getEntityType();
        if (entityType.hasSingleIdAttribute()) {
            Optional<Attribute<T,?>> idAttribute =
                    entityType.getDeclaredAttributes().stream().
                            filter(a -> entityType.getSingularAttribute(a.getName()).isId()).findFirst();
            if (idAttribute.isPresent()) {
                result = idAttribute.get().getName();
            }
        }

        return result;
    }

    protected Serializable save(Object object) {
        return getCurrentSession().save(object);
    }

    protected String delete(Object object) {
        String result = null;

        if (object != null) {
            try {
                getCurrentSession().delete(object);
            } catch (Exception e) {
                result = e.getMessage();
            }
        }

        return result;
    }

    protected List<T> findAllObjects() {
        Query<T> query = getCurrentSession().createQuery(SqlExpressions.fromExpression(
                getEntityName(), getDefaultOrderByCondition()), getGenericClass());

        return query.list();
    }


    protected T findObjectByFieldCondition(String fieldName, Object value) {
        Query<T> query = getCurrentSession().createQuery(SqlExpressions.fromExpressionWithFieldCondition(
                getEntityName(), fieldName, String.format(":%s", fieldName)), getGenericClass());
        query.setParameter(fieldName, value);

        return query.uniqueResult();
    }

    protected T findObjectById(int id) {
        return findObjectByFieldCondition(getEntityIdAttributeName(), id);
    }

    protected T findObjectByName(String name) {
        return findObjectByFieldCondition(nameAttributeName, name);
    }
}
