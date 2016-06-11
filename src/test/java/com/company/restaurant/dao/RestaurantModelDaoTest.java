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
    private static CourseDao courseDao;
    private static MenuDao menuDao;
    private static TableDao tableDao;
    private static CookedCourseDao cookedCourseDao;
    private static OrderDao orderDao;
    private static OrderCourseDao orderCourseDao;
    private static IngredientDao ingredientDao;
    private static PortionDao portionDao;
    private static WarehouseDao warehouseDao;

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

    protected static void initDataSource(String configLocation) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);

        jobPositionDao = applicationContext.getBean(JobPositionDao.class);
        menuDao = applicationContext.getBean(MenuDao.class);
        employeeDao = applicationContext.getBean(EmployeeDao.class);
        courseCategoryDao = applicationContext.getBean(CourseCategoryDao.class);
        courseDao = applicationContext.getBean(CourseDao.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDataSource(null); // intentionally, to generate exception if use this code directly
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
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

    @Test(timeout = 2000)
    public void addFindDelCourseTest() throws Exception {
        String name = Util.getRandomString();
        Course course = new Course();
        course.setCategoryId(courseCategoryId());
        course.setName(name);
        course.setWeight(Util.getRandomFloat());
        course.setCost(Util.getRandomFloat());
        course = courseDao.addCourse(course);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseByName(course.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseById(course.getCourseId())));

        courseDao.delCourse(name);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete by "the whole object"
        course = courseDao.addCourse(course);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseByName(name)));
        courseDao.delCourse(course);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete of non-existent data
        courseDao.delCourse(name);

        // Whole course list
        for (Course course1 : courseDao.findAllCourses()) {
            System.out.println("Course: id: " + course1.getCourseId() + ", name: " + course1.getName());
        }
    }

    @Test(timeout = 2000)
    public void addFindDelMenuTest() throws Exception {
        String name = Util.getRandomString();
        Menu menu = menuDao.addMenu(name);

        Menu menuByName = menuDao.findMenuByName(name);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu, menuByName));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu,
                menuDao.findMenuById(menu.getId())));

        // Courses in menu ----------------------------
        String courseName1 = Util.getRandomString();
        Course course1 = new Course();
        course1.setCategoryId(courseCategoryId());
        course1.setName(courseName1);
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1 = courseDao.addCourse(course1);

        String courseName2 = Util.getRandomString();
        Course course2 = new Course();
        course2.setCategoryId(courseCategoryId());
        course2.setName(courseName2);
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2 = courseDao.addCourse(course2);

        menuDao.addCourseToMenu(menu, course1);
        menuDao.addCourseToMenu(menu, course2);

        for (MenuCourseView menuCourseList : menuDao.findMenuCourses(menu)) {
            menuDao.findMenuCourseByCourseId(menu, menuCourseList.getCourseId());
            System.out.println(menuCourseList.getCourseName() + ": " + menuCourseList.getCourseCategoryName());
        }


        menuDao.delCourseFromMenu(menu, course1);
        menuDao.delCourseFromMenu(menu, course2);

        courseDao.delCourse(courseName1);
        courseDao.delCourse(courseName2);
        // ----------------------------

        for (Menu m : menuDao.findAllMenus()) {
            System.out.println("menu_id: " + m.getId() + ", name: " + m.getName());
        }

        menuDao.delMenu(name);
        assertTrue(menuDao.findMenuByName(name) == null);
    }
}
