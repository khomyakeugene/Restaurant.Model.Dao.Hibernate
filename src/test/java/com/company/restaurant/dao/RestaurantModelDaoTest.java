package com.company.restaurant.dao;

import com.company.restaurant.model.JobPosition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yevhen on 08.06.2016.
 */
public abstract class RestaurantModelDaoTest {
    private static JobPositionDao jobPositionDao;

    public static void initEnvironment() throws Exception {
    }

    private static void tearDownEnvironment() throws Exception {
    }

    protected static void initDataSource(String configLocation) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);

        jobPositionDao = applicationContext.getBean(JobPositionDao.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDataSource(null); // intentionally, to generate exception if use this code directly

        initEnvironment();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        tearDownEnvironment();
    }

    @Test(timeout = 2000)
    public void addFindDelJobPosition() throws Exception {
        String name = Util.getRandomString();
        JobPosition jobPosition = jobPositionDao.addJobPosition(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                jobPositionDao.findJobPositionByName(jobPosition.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                jobPositionDao.findJobPositionById(jobPosition.getId())));

        jobPositionDao.delJobPosition(name);
        assertTrue(jobPositionDao.findJobPositionByName(name) == null);
        // Test delete of non-existent data
        jobPositionDao.delJobPosition(name);
    }

}
