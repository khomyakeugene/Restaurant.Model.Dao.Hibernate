package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.SqlExpressions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.hibernate.query.Query;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoEntity<T> {
    private static final String ORDER_BY_CONDITION_PATTERN = "ORDER BY %s";

    private Class<T> entityClass;
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

    public Class<T> getEntityClass() {
        if (entityClass == null) {
            entityClass = getGenericClass();
        }

        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected T newObject() {
        Class entityClass = getEntityClass();

        T object;
        try {
            object = (T) Class.forName(entityClass.getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    protected boolean isPersistent(T object) {
        Session session = getCurrentSession();
        String entityName = object.getClass().getName();

        try {
            // Method <getIdentifier> could throw <TransientObjectException> if the instance is transient or
            // associated with a different session
            Serializable id = session.getIdentifier(object);
            // Try to use session.get()
            return (session.get(entityName, id) != null);
        } catch (TransientObjectException e) {
            // In the case of <TransientObjectException> try to directly use value of <identifier>-attribute
            // (just for the "first level", without checking for superclasses of object.getClass())
            try {
                Field idField = object.getClass().getDeclaredField(getEntityIdAttributeName());
                idField.setAccessible(true);
                try {
                    // It is important to give <id> of particular class for method session.get(), otherwise
                    // <TypeMismatchException> with a message such "Provided id of the wrong type for
                    // class com.company.restaurant.model.Employee. Expected: class java.lang.Integer,
                    // got class java.lang.Long" should be generated
                    Type idFieldType = idField.getType();
                    // Check only for primitive types such "int" and "long"
                    if (((Class)idFieldType).getName().equals(int.class.getName())) {
                        return (session.get(entityName, idField.getInt(object)) != null);
                    } else if (((Class)idFieldType).getName().equals(long.class.getName())) {
                        return (session.get(entityName, idField.getLong(object)) != null);
                    }
                    // Intentionally do not check for other possible types
                    return false;
                } catch (IllegalAccessException | IllegalArgumentException e2) {
                    // In this case just dare to suggest that <object> is not persistent
                    return false;
                }
            } catch (NoSuchFieldException e1) {
                // In this case just dare to suggest that <object> is not persistent
                return false;
            }
        }
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private String getEntityName() {
        return getEntityClass().getName();
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
        return sessionFactory.getMetamodel().entity(getEntityClass());
    }

    private String getEntityIdAttributeName() {
        String result = null;

        EntityType<T> entityType = getEntityType();
        if (entityType.hasSingleIdAttribute()) {
            Optional<Attribute<T, ?>> idAttribute =
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
            } catch (Throwable e) {
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
                getEntityName(), getDefaultOrderByCondition()), getEntityClass());

        return query.list();
    }


    protected List<T> findObjectsByAttributeValue(String attributeName, Object value) {
        Query<T> query = getCurrentSession().createQuery(SqlExpressions.fromExpressionWithFieldCondition(
                getEntityName(), attributeName, String.format(":%s", attributeName)), getEntityClass());
        query.setParameter(attributeName, value);

        return query.list();
    }


    protected T findObjectByAttributeValue(String attributeName, Object value) {
        T result = null;

        List<T> objects = findObjectsByAttributeValue(attributeName, value);
        if (objects != null && objects.size() > 0) {
            result = objects.get(0);
        }

        return result;
    }

    protected T findObjectById(int id) {
        return findObjectByAttributeValue(getEntityIdAttributeName(), id);
    }

    protected T findObjectByName(String name) {
        return findObjectByAttributeValue(nameAttributeName, name);
    }

    protected List<T> findObjectsByTwoAttributeValues(String attributeName1,
                                                      Object value1,
                                                      String attributeName2,
                                                      Object value2) {
        Query<T> query = getCurrentSession().createQuery(SqlExpressions.fromExpressionWithTwoFieldCondition(
                getEntityName(), attributeName1, String.format(":%s", attributeName1), attributeName2,
                String.format(":%s", attributeName2)), getEntityClass());
        query.setParameter(attributeName1, value1);
        query.setParameter(attributeName2, value2);

        return query.list();
    }
}
