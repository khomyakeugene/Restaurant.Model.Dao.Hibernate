package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.model.LinkObject;

import java.lang.reflect.Field;

/**
 * Created by Yevhen on 11.06.2016.
 */
public abstract class HDaoLinkEntity<T extends LinkObject> extends HDaoEntity<T> {
    private static final String FIRST_ID_ATTRIBUTE_NAME = "firstId";
    private static final String SECOND_ID_ATTRIBUTE_NAME = "secondId";
    private static final String LINK_DATA_ATTRIBUTE_NAME = "linkData";

    protected String firstIdAttributeName = FIRST_ID_ATTRIBUTE_NAME;
    protected String secondIdAttributeName = SECOND_ID_ATTRIBUTE_NAME;
    protected String LinkDataAttributeNameName = LINK_DATA_ATTRIBUTE_NAME;

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
        T object = newObject();
        object.setFirstId(firstId);
        object.setSecondId(secondId);

        return delete(object);
    }

    protected String selectLinkData(int firstId, int secondId) {
        String result = null;

        T object = findObjectByTwoAttributeValues(firstIdAttributeName, firstId, secondIdAttributeName, secondId);
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
