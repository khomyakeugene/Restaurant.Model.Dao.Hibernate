package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.model.SimpleDic;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HDaoEntitySimpleDic<T extends SimpleDic> extends HDaoEntity<T> {
    private static final String NAME_ATTRIBUTE_NAME = "name";

    @Override
    protected void initMetadata() {
        this.nameAttributeName = NAME_ATTRIBUTE_NAME;
        this.orderByCondition = getOrderByCondition(nameAttributeName);
    }
}
