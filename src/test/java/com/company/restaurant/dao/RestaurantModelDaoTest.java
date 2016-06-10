package com.company.restaurant.dao;

import com.company.restaurant.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yevhen on 08.06.2016.
 */
public abstract class RestaurantModelDaoTest {
    private final static String DUPLICATE_KEY_VALUE_VIOLATES_MESSAGE = "duplicate key value violates";

    private static JobPositionDao jobPositionDao;
    private static EmployeeDao employeeDao;
    private static CourseCategoryDao courseCategoryDao;
    private static MenuDao menuDao;
    private static TableDao tableDao;
    private static CourseDao courseDao;
    private static CookedCourseDao cookedCourseDao;
    private static OrderDao orderDao;
    private static OrderCourseDao orderCourseDao;
    private static IngredientDao ingredientDao;
    private static PortionDao portionDao;
    private static WarehouseDao warehouseDao;

    private static int closedOrderId;
    private static Order closedOrder;
    private static String closedOrderCourseName1;
    private static Course closedOrderCourse1;
    private static String closedOrderCourseName2;
    private static Course closedOrderCourse2;
    private static Course testCourse;

    private static Employee employee() {
        return employeeDao.findAllEmployees().get(0);
    }

    private static int jobPositionId() {
        return jobPositionDao.findAllJobPositions().get(0).getId();
    }

    private static int courseCategoryId() {
        return courseCategoryDao.findAllCourseCategories().get(0).getId();
    }

    private static int employeeId() {
        return employee().getEmployeeId();
    }

    private static int tableId() {
        return tableDao.findAllTables().get(0).getTableId();
    }

    private static int lastTableId() {
        List<Table> tableList = tableDao.findAllTables();

        return tableList.get(tableList.size()-1).getTableId();
    }

    private static Course prepareTestCourse() {
        testCourse = new Course();
        testCourse.setCategoryId(courseCategoryId());
        testCourse.setName(Util.getRandomString());
        testCourse.setWeight(Util.getRandomFloat());
        testCourse.setCost(Util.getRandomFloat());

        testCourse = courseDao.addCourse(testCourse);

        return testCourse;
    }

    private static void delTestCourse() {
        courseDao.delCourse(testCourse);
    }

    private static void prepareClosedOrder() throws Exception {
        Order order = new Order();
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        order.setStateType("A");
        closedOrderId = orderDao.addOrder(order).getOrderId();

        // Courses for closed order ----------------------------
        closedOrderCourseName1 = Util.getRandomString();
        closedOrderCourse1 = new Course();
        closedOrderCourse1.setCategoryId(courseCategoryId());
        closedOrderCourse1.setName(closedOrderCourseName1);
        closedOrderCourse1.setWeight(Util.getRandomFloat());
        closedOrderCourse1.setCost(Util.getRandomFloat());
        closedOrderCourse1 = courseDao.addCourse(closedOrderCourse1);

        closedOrderCourseName2 = Util.getRandomString();
        closedOrderCourse2 = new Course();
        closedOrderCourse2.setCategoryId(courseCategoryId());
        closedOrderCourse2.setName(closedOrderCourseName2);
        closedOrderCourse2.setWeight(Util.getRandomFloat());
        closedOrderCourse2.setCost(Util.getRandomFloat());
        closedOrderCourse2 = courseDao.addCourse(closedOrderCourse2);
        // ----------

        orderCourseDao.addCourseToOrder(order, closedOrderCourse1, 1);

        closedOrder = orderDao.updOrderState(order, "B");
    }

    private static void clearClosedOrder() throws Exception {
        Order order = orderDao.findOrderById(closedOrderId);
        if (order != null) {
            // Manually change order state to "open"
            order = orderDao.updOrderState(order, "A");
            // Delete "open" order
            orderDao.delOrder(order);
        }

        // Delete course for closed order
        courseDao.delCourse(closedOrderCourseName1);
        courseDao.delCourse(closedOrderCourseName2);
    }

    public static void initEnvironment() throws Exception {
    }

    private static void tearDownEnvironment() throws Exception {
    }

    protected static void initDataSource(String configLocation) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);

        jobPositionDao = applicationContext.getBean(JobPositionDao.class);
        menuDao = applicationContext.getBean(MenuDao.class);
        employeeDao = applicationContext.getBean(EmployeeDao.class);
        courseCategoryDao = applicationContext.getBean(CourseCategoryDao.class);
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

        for (JobPosition jP : jobPositionDao.findAllJobPositions()) {
            System.out.println("Job position Id :" + jP.getId() +
                    ", Job position name :" + jP.getName());
        }
    }

    @Test(timeout = 2000)
    public void addFindDelEmployeeTest() throws Exception {
        String firstName = Util.getRandomString();
        String secondName = Util.getRandomString();
        Employee employee = new Employee();
        employee.setJobPositionId(jobPositionId());
        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setPhoneNumber(Util.getRandomString());
        employee.setSalary(Util.getRandomFloat());

        employee = employeeDao.addEmployee(employee);
        int employeeId = employee.getEmployeeId();

        // Select test <employee> and check
        Employee employeeByFirstName = employeeDao.findEmployeeByFirstName(firstName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstName));

        Employee employeeBySecondName = employeeDao.findEmployeeBySecondName(secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeBySecondName));

        Employee employeeByFirstAndSecondName =
                employeeDao.findEmployeeByFirstAndSecondName(firstName, secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstAndSecondName));

        Employee employeeById = employeeDao.findEmployeeById(employeeId);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeById));

        employeeDao.delEmployee(employee);
        assertTrue(employeeDao.findEmployeeById(employeeId) == null);
    }

    @Test(timeout = 2000)
    public void addFindDelCourseCategoryTest() throws Exception {
        String name = Util.getRandomString();
        CourseCategory courseCategory = courseCategoryDao.addCourseCategory(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseCategoryDao.findCourseCategoryByName(courseCategory.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseCategoryDao.findCourseCategoryById(courseCategory.getId())));

        courseCategoryDao.delCourseCategory(name);
        assertTrue(courseCategoryDao.findCourseCategoryByName(name) == null);
        // Test delete of non-existent data
        courseCategoryDao.delCourseCategory(name);
    }
}
