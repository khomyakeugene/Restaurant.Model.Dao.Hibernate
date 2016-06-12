package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.MenuCourseDao;
import com.company.restaurant.dao.hibernate.proto.HDaoLinkEntity;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;
import com.company.restaurant.model.MenuCourse;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yevhen on 11.06.2016.
 */
public class HMenuCourseDao extends HDaoLinkEntity<MenuCourse> implements MenuCourseDao {
    private static final String MENU_ID_ATTRIBUTE_NAME = "menuId";
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        firstIdAttributeName = MENU_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = COURSE_ID_ATTRIBUTE_NAME;
    }

    private int getMaxCourseNumberInMenu(Menu menu) {
        /*
        String selectResult = find
                getOneFieldByFieldCondition(
                maxFieldValueExpression(COURSE_NUMBER_FIELD_NAME),
                firstIdFieldName, menu.getId());

        return (selectResult == null) || selectResult.equals("") ? 0 : Integer.parseInt(selectResult);
        */

        return 0;
    }

    @Transactional
    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        saveOrUpdate(menu.getId(), course.getCourseId(),
                Integer.toString(getMaxCourseNumberInMenu(menu) + 1));

    }

    @Transactional
    @Override
    public String delCourseFromMenu(Menu menu, Course course) {
        return delete(menu.getId(), course.getCourseId());
    }
}
