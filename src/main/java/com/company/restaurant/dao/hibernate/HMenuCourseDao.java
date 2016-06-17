package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.MenuCourseDao;
import com.company.restaurant.dao.hibernate.proto.HDaoLinkEntity;
import com.company.restaurant.dao.proto.SqlExpressions;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;
import com.company.restaurant.model.MenuCourse;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yevhen on 11.06.2016.
 */
public class HMenuCourseDao extends HDaoLinkEntity<MenuCourse> implements MenuCourseDao {
    private static final String MENU_ID_ATTRIBUTE_NAME = "menuId";
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";
    private static final String COURSE_NUMBER_ATTRIBUTE_NAME = "courseNumber";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        firstIdAttributeName = MENU_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = COURSE_ID_ATTRIBUTE_NAME;
    }

    private int getMaxCourseNumberInMenu(Menu menu) {
        Query<Integer> query = getCurrentSession().createQuery(SqlExpressions.selectExpression(
                SqlExpressions.maxFieldValueExpression(COURSE_NUMBER_ATTRIBUTE_NAME)) +
                SqlExpressions.fromExpression(getEntityName(), SqlExpressions.whereExpression(
                        SqlExpressions.equalityCondition(MENU_ID_ATTRIBUTE_NAME, menu.getId())), null), Integer.class);
        Integer result = query.uniqueResult();

        return (result == null) ? 0 : result;
    }

    @Transactional
    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        saveOrUpdate(menu.getId(), course.getCourseId(),
                Integer.toString(getMaxCourseNumberInMenu(menu) + 1));

    }

    @Transactional
    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        delete(menu.getId(), course.getCourseId());
    }
}
