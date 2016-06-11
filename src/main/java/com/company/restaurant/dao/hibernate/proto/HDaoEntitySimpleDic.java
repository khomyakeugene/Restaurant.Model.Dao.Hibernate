package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.model.SimpleDic;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HDaoEntitySimpleDic<T extends SimpleDic> extends HDaoEntity<T> {
    @Override
    protected void initMetadata() {
        this.orderByCondition = getOrderByCondition(nameAttributeName);
    }

    protected T save(String name) {
        T object = newObject();
        object.setName(name);

        return save(object);
    }
}
