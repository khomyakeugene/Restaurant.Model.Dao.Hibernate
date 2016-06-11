package com.company.restaurant.dao;

import org.junit.BeforeClass;

/**
 * Created by Yevhen on 20.05.2016.
 */
public class RestaurantModelDaoHTest extends RestaurantModelDaoTest {
    private final static String APPLICATION_CONTEXT_NAME = "restaurant-hibernate-context.xml";

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDataSource(APPLICATION_CONTEXT_NAME);
    }
}