package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.SqlExpressions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoEntity<T> {
    private static final String ORDER_BY_CONDITION_PATTERN = "ORDER BY %s";

    private Class entityClass;
    private SessionFactory sessionFactory;

    protected String nameAttributeName;
    protected String orderByCondition;

    public HDaoEntity() {
        initMetadata();
    }

    protected abstract void initMetadata();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Class<T> getGenericClass() {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0]);
    }

    public Class getEntityClass() {
        if (entityClass == null) {
            entityClass = getGenericClass();
        }

        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    protected T newObject() {
        Class entityClass = getEntityClass();

        T object;
        try {
            object = (T)Class.forName(entityClass.getSimpleName()).getConstructor(entityClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    protected String getOrderByCondition(String attributeName) {
        return String.format(ORDER_BY_CONDITION_PATTERN, attributeName);
    }

    private String getDefaultOrderByCondition() {
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

    protected T save(T object) {
        getCurrentSession().save(object);

        return object;
    }

    protected String delete(T object) {
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

    protected String delete(int id) {
        return delete(findObjectById(id));
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
