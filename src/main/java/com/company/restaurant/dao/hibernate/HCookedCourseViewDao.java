package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.CookedCourseDao;
import com.company.restaurant.dao.CookedCourseViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntity;
import com.company.restaurant.model.CookedCourse;
import com.company.restaurant.model.CookedCourseView;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Employee;
import com.company.restaurant.util.ObjectService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 14.06.2016.
 */
public class HCookedCourseViewDao extends HDaoEntity<CookedCourseView> implements CookedCourseViewDao {
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";
    private static final String EMPLOYEE_ID_ATTRIBUTE_NAME = "employeeId";
    private static final String COOK_DATETIME_ATTRIBUTE_NAME = "cookDatetime";

    private CookedCourseDao cookedCourseDao;

    public void setCookedCourseDao(CookedCourseDao cookedCourseDao) {
        this.cookedCourseDao = cookedCourseDao;
    }

    @Override
    protected void initMetadata() {
        this.orderByCondition = getOrderByCondition(COOK_DATETIME_ATTRIBUTE_NAME);
    }

    /*
    @Transactional
    public CookedCourseView getCookedCourseView(CookedCourse cookedCourse) {
        String s = SqlExpressions.fromExpression(getEntityName(),
                SqlExpressions.whereExpression(
                        SqlExpressions.andCondition(
                                SqlExpressions.andCondition(
                                        SqlExpressions.equalityCondition(COURSE_ID_ATTRIBUTE_NAME,
                                                String.format(":%s", COURSE_ID_ATTRIBUTE_NAME)),
                                        SqlExpressions.equalityCondition(EMPLOYEE_ID_ATTRIBUTE_NAME,
                                                String.format(":%s", EMPLOYEE_ID_ATTRIBUTE_NAME))),
                                SqlExpressions.equalityCondition(COOK_DATETIME_ATTRIBUTE_NAME,
                                        String.format(":%s", COOK_DATETIME_ATTRIBUTE_NAME)))), null);
        Query<CookedCourseView> query = getCurrentSession().createQuery(s, CookedCourseView.class);
        query.setParameter(COURSE_ID_ATTRIBUTE_NAME, cookedCourse.getCourseId());
        query.setParameter(EMPLOYEE_ID_ATTRIBUTE_NAME, cookedCourse.getEmployeeId());
        query.setParameter(COOK_DATETIME_ATTRIBUTE_NAME, cookedCourse.getCookDatetime());
        CookedCourseView cookedCourseView = query.uniqueResult();

        return cookedCourseView;
    }
    */

    @Override
    public CookedCourseView addCookedCourse(Course course, Employee employee, Float weight) {
        CookedCourse cookedCourse = cookedCourseDao.addCookedCourse(course, employee, weight);
        /*
        // Unfortunately,  t is impossible to use "SELECT CCV FROM CookedCourseView" until this
        //  "hiberante-transactional" method <CookedCourseView.addCookedCourse> has not finished, because the "new data"
        // is not accessible until hiberante-transaction is not committed.
        /* Maybe, it is not incorrectly to leave commented code, but let this "SELECT"-code will be here
        as a example of an "attempt". Also, if trying to not define this method <CookedCourseView.addCookedCourse>
        method as NON @Transactional, and then try to use separate method <getCookedCourseView>,

        CookedCourseView cookedCourseView = getCookedCourseView(cookedCourse);

        either it is @Transactional nor not, the exception
        org.hibernate.HibernateException: Could not obtain transaction-synchronized Session for current thread
        is generated :(
*/
        // "Partial solution"
        CookedCourseView cookedCourseView = (CookedCourseView)ObjectService.copyObjectByAccessors(cookedCourse,
                new CookedCourseView());
        // Then, it is need to get "other" <CookedCourseView>-fields manually ... "Right now (15.06.2016)" just mark
        // it as TO-DO

        return cookedCourseView;
    }

    @Transactional
    @Override
    public void delCookedCourse(CookedCourseView cookedCourseView) {
        cookedCourseDao.delCookedCourse((CookedCourse) ObjectService.copyObjectByAccessors(
                cookedCourseView, new CookedCourse()));
    }

    @Transactional
    @Override
    public List<CookedCourseView> findAllCookedCourses() {
        return findAllObjects();
    }
}
