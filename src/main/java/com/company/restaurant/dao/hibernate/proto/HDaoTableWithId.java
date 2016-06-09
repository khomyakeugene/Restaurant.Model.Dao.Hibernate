package com.company.restaurant.dao.hibernate.proto;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoTableWithId<T> extends HDaoTable<T> {
    protected String idFieldName;
    protected String nameFieldName;

    protected T findObjectByName(String name) {
        return findObjectByFieldCondition(nameFieldName, name);
    }

}
