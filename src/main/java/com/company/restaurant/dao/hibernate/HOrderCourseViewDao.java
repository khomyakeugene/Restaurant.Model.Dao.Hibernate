package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.OrderCourseDao;
import com.company.restaurant.dao.OrderCourseViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoQuantityLinkEntity;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;
import com.company.restaurant.model.OrderCourseView;
import com.company.restaurant.model.OrderView;
import com.company.restaurant.util.ObjectService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 13.06.2016.
 */
public class HOrderCourseViewDao extends HDaoQuantityLinkEntity<OrderCourseView>
        implements OrderCourseViewDao {
    private static final String ORDER_ID_ATTRIBUTE_NAME = "orderId";
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";

    private OrderCourseDao orderCourseDao;

    public void setOrderCourseDao(OrderCourseDao orderCourseDao) {
        this.orderCourseDao = orderCourseDao;
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();

        firstIdAttributeName = COURSE_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = ORDER_ID_ATTRIBUTE_NAME;
    }

    @Transactional
    @Override
    public void addCourseToOrder(OrderView orderView, Course course, int quantity) {
        orderCourseDao.addCourseToOrder((Order)ObjectService.copyObjectByAccessors(orderView, new Order()),
                course, quantity);

    }

    @Transactional
    @Override
    public void takeCourseFromOrder(OrderView orderView, Course course, int quantity) {
        orderCourseDao.takeCourseFromOrder((Order)ObjectService.copyObjectByAccessors(orderView, new Order()),
                course, quantity);

    }

    @Transactional
    @Override
    public List<OrderCourseView> findAllOrderCourses(OrderView orderView) {
        return findObjectsByAttributeValue(ORDER_ID_ATTRIBUTE_NAME, orderView.getOrderId());
    }

    @Transactional
    @Override
    public OrderCourseView findOrderCourseByCourseId(OrderView orderView, int courseId) {
        return findObjectByTwoAttributeValues(courseId, orderView.getOrderId());
    }
}
