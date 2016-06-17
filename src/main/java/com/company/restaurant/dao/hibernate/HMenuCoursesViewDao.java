package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.MenuCourseDao;
import com.company.restaurant.dao.MenuCoursesViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoLinkEntity;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;
import com.company.restaurant.model.MenuCourseView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 11.06.2016.
 */
public class HMenuCoursesViewDao extends HDaoLinkEntity<MenuCourseView> implements MenuCoursesViewDao {
    private static final String MENU_ID_ATTRIBUTE_NAME = "menuId";
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";

    private MenuCourseDao menuCourseDao;

    public void setMenuCourseDao(MenuCourseDao menuCourseDao) {
        this.menuCourseDao = menuCourseDao;
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();

        firstIdAttributeName = MENU_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = COURSE_ID_ATTRIBUTE_NAME;
    }

    @Transactional
    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        menuCourseDao.addCourseToMenu(menu, course);
    }

    @Transactional
    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        menuCourseDao.delCourseFromMenu(menu, course);
    }

    @Transactional
    @Override
    public List<MenuCourseView> findMenuCourses(Menu menu) {
        return findObjectsByAttributeValue(firstIdAttributeName, menu.getId());
    }

    @Transactional
    @Override
    public MenuCourseView findMenuCourseByCourseId(Menu menu, int courseId) {
        return findObjectByTwoAttributeValues(menu.getId(), courseId);
    }
}
