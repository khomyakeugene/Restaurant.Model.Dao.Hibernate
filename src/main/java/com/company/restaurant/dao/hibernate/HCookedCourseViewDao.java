package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.CookedCourseViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntity;
import com.company.restaurant.model.CookedCourseView;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Employee;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 14.06.2016.
 */
public class HCookedCourseViewDao extends HDaoEntity<CookedCourseView> implements CookedCourseViewDao {
    private static final String COOK_DATETIME_ATTRIBUTE_NAME = "cookDatetime";

    @Override
    protected void initMetadata() {
        this.orderByCondition = getOrderByCondition(COOK_DATETIME_ATTRIBUTE_NAME);

    }

    @Transactional
    @Override
    public CookedCourseView addCookedCourse(Course course, Employee employee, Float weight) {
        return null;
    }

    @Transactional
    @Override
    public String delCookedCourse(CookedCourseView cookedCourseView) {
        return null;
    }

    @Transactional
    @Override
    public List<CookedCourseView> findAllCookedCourses() {
        return findAllObjects();
    }
}
