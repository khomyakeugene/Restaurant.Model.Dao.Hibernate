package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.model.LinkObject;

/**
 * Created by Yevhen on 11.06.2016.
 */
public abstract class HDaoLinkEntity<T extends LinkObject> extends HDaoEntity<T> {
    private static final String FIRST_ID_ATTRIBUTE_NAME = "firstId";
    private static final String SECOND_ID_ATTRIBUTE_NAME = "secondId";
    private static final String LINK_DATA_ATTRIBUTE_NAME = "linkData";

    protected String firstIdAttributeName;
    protected String secondIdAttributeName;
    protected String linkDataAttributeName;

    @Override
    protected void initMetadata() {
        firstIdAttributeName = FIRST_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = SECOND_ID_ATTRIBUTE_NAME;
        linkDataAttributeName = LINK_DATA_ATTRIBUTE_NAME;
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

    protected T update(int firstId, int secondId, String linkData) {
        return update(newObject(firstId, secondId, linkData));
    }

    protected void delete(int firstId, int secondId) {
        T object = newObject();
        object.setFirstId(firstId);
        object.setSecondId(secondId);

        delete(object);
    }

    protected T findObjectByTwoAttributeValues(int firstId, int secondId) {
        return findObjectByTwoAttributeValues(firstIdAttributeName, firstId, secondIdAttributeName, secondId);
    }

    protected String selectLinkData(int firstId, int secondId) {
        String result = null;

        T object = findObjectByTwoAttributeValues(firstId, secondId);
        if (object != null) {
            result = object.getLinkData();
        }

        return result;
    }
}
