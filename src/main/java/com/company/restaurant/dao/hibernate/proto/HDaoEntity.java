package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.SqlExpressions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
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
    private static final String SQL_ORDER_BY_CONDITION_PATTERN = "ORDER BY %s";
    private static final String NAME_ATTRIBUTE_NAME = "name";

    private Class<T> entityClass;
    private SessionFactory sessionFactory;

    protected String nameAttributeName = NAME_ATTRIBUTE_NAME;
    protected String orderByCondition;

    public HDaoEntity() {
        initMetadata();
    }

    protected abstract void initMetadata();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected T getFirstFromList(List<T> objects) {
        return  (objects != null && objects.size() > 0) ? objects.get(0) : null;
    }

    private Class<T> getGenericClass() {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0]);
    }

    protected Class<T> getEntityClass() {
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
                    if (((Class) idFieldType).getName().equals(int.class.getName())) {
                        return (session.get(entityName, idField.getInt(object)) != null);
                    } else if (((Class) idFieldType).getName().equals(long.class.getName())) {
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

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected String getEntityName() {
        return getEntityClass().getName();
    }

    protected String getOrderByCondition(String attributeName) {
        return String.format(SQL_ORDER_BY_CONDITION_PATTERN, attributeName);
    }

    protected String getDefaultOrderByCondition() {
        if (orderByCondition == null) {
            orderByCondition = getOrderByCondition(getEntityIdAttributeName());
        }

        return orderByCondition;
    }

    private EntityType<T> getEntityType() {
        return sessionFactory.getMetamodel().entity(getEntityClass());
    }

    protected String getTableName() {
        String result = null;

        // sessionFactory.getMetamodel().managedType(getEntityClass()).
        // Do not know how to get ClassMetadata (or "something like" <AbstractEntityPersister>) using
        // "non deprecated" method :( ; at least, don't understand how can use, for example,
        // sessionFactory.getMetamodel() approaching the same aim ...
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(getEntityClass());
        if (classMetadata instanceof AbstractEntityPersister) {  // And what I have to do if "not instnceof ..."?
            AbstractEntityPersister abstractEntityPersister = (AbstractEntityPersister) classMetadata;
            result = abstractEntityPersister.getTableName();
            // Without possibly presented schema-name
            int pointIndex = result.lastIndexOf('.');
            if (pointIndex != -1) {
                result = result.substring(pointIndex+1);
            }
        }

        return result;
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

    protected T saveOrUpdate(T object) {
        Session session = getCurrentSession();

        // It is important to call session .clear(): otherwise, the exception raises:
        // org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already
        // associated with the session : [............
        session.clear();
        session.saveOrUpdate(object);

        return object;
    }

    protected T update(T object) {
        Session session = getCurrentSession();

        // It is important to call session .clear(): otherwise, the exception raises:
        // org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already
        // associated with the session : [............
        session.clear();
        session.update(object);

        return object;
    }

    protected void delete(T object) {
        if (object != null) {
            Session session = getCurrentSession();
            // It is important to call session .clear(): otherwise, sometimes (not all the time!) (cannot understand
            // the reason of it, but it is!) the exception could raise:
            // Could not commit Hibernate transaction; nested exception is org.hibernate.TransactionException: Transaction
            // was marked for rollback only; cannot commit
            session.clear();
            session.delete(object);
        }
    }

    protected void delete(int id) {
        delete(findObjectById(id));
    }

    protected void delete(String name) {
        delete(findObjectByName(name));
    }

    protected List<T> findObjects(String whereCondition) {
        Query<T> query = getCurrentSession().createQuery(SqlExpressions.fromExpression(
                getEntityName(), SqlExpressions.whereExpression(whereCondition),
                getDefaultOrderByCondition()), getEntityClass());

        return query.list();
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
        return getFirstFromList(findObjectsByAttributeValue(attributeName, value));
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

    protected T findObjectByTwoAttributeValues(String attributeName1,
                                               Object value1,
                                               String attributeName2,
                                               Object value2) {
        return getFirstFromList(findObjectsByTwoAttributeValues(attributeName1, value1, attributeName2, value2));
    }
}
