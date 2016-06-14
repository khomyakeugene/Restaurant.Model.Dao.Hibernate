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
    private static final String COOK_DATETIME_ATTRIBUTE_NAME = "cookDatetime";

    private CookedCourseDao cookedCourseDao;

    public void setCookedCourseDao(CookedCourseDao cookedCourseDao) {
        this.cookedCourseDao = cookedCourseDao;
    }

    @Override
    protected void initMetadata() {
        this.orderByCondition = getOrderByCondition(COOK_DATETIME_ATTRIBUTE_NAME);
    }

    @Transactional
    @Override
    public CookedCourseView addCookedCourse(Course course, Employee employee, Float weight) {
        CookedCourse cookedCourse = cookedCourseDao.addCookedCourse(course, employee, weight);
        CookedCourseView cookedCourseView = (CookedCourseView) ObjectService.copyObjectByAccessors(
                cookedCourse, new CookedCourseView());

        // Select "full state"


        return cookedCourseView;
    }

    @Transactional
    @Override
    public String delCookedCourse(CookedCourseView cookedCourseView) {
        return cookedCourseDao.delCookedCourse((CookedCourse) ObjectService.copyObjectByAccessors(
                cookedCourseView, new CookedCourse()));
    }

    @Transactional
    @Override
    public List<CookedCourseView> findAllCookedCourses() {
        return findAllObjects();
    }
}
