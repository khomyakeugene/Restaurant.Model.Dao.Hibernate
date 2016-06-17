package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.MenuCoursesViewDao;
import com.company.restaurant.dao.MenuDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntitySimpleDic;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;
import com.company.restaurant.model.MenuCourseView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 10.06.2016.
 */
public class HMenuDao extends HDaoEntitySimpleDic<Menu> implements MenuDao {
    private MenuCoursesViewDao menuCoursesViewDao;

    public void setMenuCoursesViewDao(MenuCoursesViewDao menuCoursesViewDao) {
        this.menuCoursesViewDao = menuCoursesViewDao;
    }

    @Transactional
    @Override
    public Menu addMenu(String name) {
        return save(name);
    }

    @Transactional
    @Override
    public void delMenu(String name) {
        delete(name);
    }

    @Transactional
    @Override
    public void delMenu(Menu menu) {
        delete(menu);
    }

    @Transactional
    @Override
    public Menu findMenuByName(String name) {
        return findObjectByName(name);
    }

    @Transactional
    @Override
    public Menu findMenuById(int menuId) {
        return findObjectById(menuId);
    }

    @Transactional
    @Override
    public List<Menu> findAllMenus() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        menuCoursesViewDao.addCourseToMenu(menu, course);
    }

    @Transactional
    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        menuCoursesViewDao.delCourseFromMenu(menu, course);
    }

    @Transactional
    @Override
    public List<MenuCourseView> findMenuCourses(Menu menu) {
        return menuCoursesViewDao.findMenuCourses(menu);
    }

    @Transactional
    @Override
    public MenuCourseView findMenuCourseByCourseId(Menu menu, int courseId) {
        return menuCoursesViewDao.findMenuCourseByCourseId(menu, courseId);
    }
}
