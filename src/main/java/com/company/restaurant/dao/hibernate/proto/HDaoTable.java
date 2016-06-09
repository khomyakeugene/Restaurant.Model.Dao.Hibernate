package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.DaoTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

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

    protected void delete(Object object) {
        if (object != null) {
            getCurrentSession().delete(object);
        }
    }

    protected T findObjectByFieldCondition(String fieldName, Object value) {
        String entityName = getGenericName();
        String aliasName = entityName.substring(0, 1);

        Query<T> query = getCurrentSession().createQuery(fieldEntityQueryCondition(
                String.format("%s.%s", aliasName, fieldName), String.format(":%s", fieldName),
                aliasName, String.format("%s %s", entityName, aliasName)), getGenericClass());
        query.setParameter(fieldName, value);

        return query.uniqueResult();
    }

    protected T findObjectById(int id) {
        return findObjectByFieldCondition(getEntityIdAttributeName(), id);
    }
}
