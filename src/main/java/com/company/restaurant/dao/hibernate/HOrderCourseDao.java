package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.OrderCourseDao;
import com.company.restaurant.dao.hibernate.proto.HDaoQuantityLinkEntity;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;
import com.company.restaurant.model.OrderCourse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 13.06.2016.
 */
public class HOrderCourseDao extends HDaoQuantityLinkEntity<OrderCourse>
        implements OrderCourseDao {
    private static final String ORDER_ID_ATTRIBUTE_NAME = "orderId";
    private static final String COURSE_ID_ATTRIBUTE_NAME = "courseId";
    private static final String COURSE_QUANTITY_ATTRIBUTE_NAME = "courseQuantity";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        firstIdAttributeName = COURSE_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = ORDER_ID_ATTRIBUTE_NAME;
    }

    @Transactional
    @Override
    public void addCourseToOrder(Order order, Course course, int quantity) {
        increaseQuantity(course.getCourseId(), order.getOrderId(), quantity);
    }

    @Transactional
    @Override
    public void takeCourseFromOrder(Order order, Course course, int quantity) {
        decreaseQuantity(course.getCourseId(), order.getOrderId(), quantity);
    }

    @Transactional
    @Override
    public List<OrderCourse> findAllOrderCourses(Order order) {
        return findObjectsByAttributeValue(ORDER_ID_ATTRIBUTE_NAME, order.getOrderId());
    }

    @Transactional
    @Override
    public OrderCourse findOrderCourseByCourseId(Order order, int courseId) {
        return findObjectByTwoAttributeValues(courseId, order.getOrderId());
    }
}
