package com.company.restaurant.dao.hibernate.proto;

import com.company.restaurant.model.SimpleDic;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HDaoEntitySimpleDic<T extends SimpleDic> extends HDaoEntity<T> {
    private static final String NAME_FIELD_NAME = "name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY name";

    @Override
    protected void initMetadata() {
        this.nameAttributeName = NAME_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }
}
