package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.CourseCategoryDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntitySimpleDic;
import com.company.restaurant.model.CourseCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 10.06.2016.
 */
public class HCourseCategoryDao extends HDaoEntitySimpleDic<CourseCategory> implements CourseCategoryDao {
    @Transactional
    @Override
    public CourseCategory addCourseCategory(String name) {
        return save(name);
    }

    @Transactional
    @Override
    public void delCourseCategory(String name) {
        delete(name);
    }

    @Transactional
    @Override
    public CourseCategory findCourseCategoryByName(String name) {
        return findObjectByName(name);
    }

    @Transactional
    @Override
    public CourseCategory findCourseCategoryById(int courseCategoryId) {
        return findObjectById(courseCategoryId);
    }

    @Transactional
    @Override
    public List<CourseCategory> findAllCourseCategories() {
        return findAllObjects();
    }
}
