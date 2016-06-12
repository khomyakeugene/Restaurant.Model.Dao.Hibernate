package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.dao.proto.SqlExpressions;
import com.company.restaurant.model.LinkObject;
import org.hibernate.query.Query;

import java.lang.reflect.Field;

import static com.company.restaurant.dao.proto.SqlExpressions.twoFieldAndCondition;

/**
 * Created by Yevhen on 11.06.2016.
 */
public abstract class HDaoLinkEntity<T extends LinkObject> extends HDaoEntity<T> {
    private static final String FIRST_ID_ATTRIBUTE_NAME = "firstId";
    private static final String SECOND_ID_ATTRIBUTE_NAME = "secondId";
    private static final String LINK_DATA_ATTRIBUTE_NAME = "linkData";

    protected String firstIdAttributeName;
    protected String secondIdAttributeName;
    protected String LinkDataAttributeNameName;

    @Override
    protected void initMetadata() {
        firstIdAttributeName = FIRST_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = SECOND_ID_ATTRIBUTE_NAME;
        LinkDataAttributeNameName = LINK_DATA_ATTRIBUTE_NAME;
    }

    protected T newObject(int firstId, int secondId, String linkData) {
        T object = newObject();
        object.setFirstId(firstId);
        object.setSecondId(secondId);
        object.setLinkData(linkData);

        return object;
    }

    protected T save(int firstId, int secondId, String linkData) {
        return save(newObject(firstId, secondId, linkData));
    }

    protected T saveOrUpdate(int firstId, int secondId, String linkData) {
        return saveOrUpdate(newObject(firstId, secondId, linkData));
    }

    protected String delete(int firstId, int secondId) {
        String result = null;

        /*
        Cannot understand why, but using <delete(object)> constantly raises exception such as
         org.springframework.transaction.TransactionSystemException: Could not commit Hibernate transaction; nested
         exception is org.hibernate.TransactionException: Transaction was marked for rollback only; cannot commit
         And, unfortunately, it I cannot also once and for all investigate which is the natural reason of such error

        T object = newObject();
        object.setFirstId(firstId);
        object.setSecondId(secondId);

        return delete(object);
         */
        Query query = getCurrentSession().createQuery(SqlExpressions.deleteExpression(getEntityName() + " " +
                twoFieldAndCondition(firstIdAttributeName, firstId, secondIdAttributeName, secondId)));
        try {
            query.executeUpdate();
        } catch (Exception e) {
            result = e.getMessage();
        }

        return result;
    }

    protected T findObjectByTwoAttributeValues(int firstId, int secondId) {
        return findObjectByTwoAttributeValues(firstIdAttributeName, firstId, secondIdAttributeName, secondId);
    }

    protected String selectLinkData(int firstId, int secondId) {
        String result = null;

        T object = findObjectByTwoAttributeValues(firstId, secondId);
        if (object != null) {
            try {
                Field linkDataField = object.getClass().getDeclaredField(LinkDataAttributeNameName);
                linkDataField.setAccessible(true);
                result = linkDataField.get(object).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
