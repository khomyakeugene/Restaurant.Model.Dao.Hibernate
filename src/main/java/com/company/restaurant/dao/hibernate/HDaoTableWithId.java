package com.company.restaurant.dao.hibernate;

/**
 * Created by Yevhen on 09.06.2016.
 */
public abstract class HDaoTableWithId<T> extends HDaoTable<T> {

    protected String idFieldName;
    protected String nameFieldName;

}