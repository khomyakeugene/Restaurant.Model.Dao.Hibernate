package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.OrderDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntity;
import com.company.restaurant.model.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 13.06.2016.
 */
public class HOrderDao extends HDaoEntity<Order> implements OrderDao {
    private static final String STATE_TYPE_ATTRIBUTE_NAME = "stateType";
    private static final String ORDER_NUMBER_ATTRIBUTE_NAME = "orderNumber";

    @Override
    protected void initMetadata() {

    }

    @Override
    public String orderEntityName() {
        return getTableName();
    }

    @Transactional
    @Override
    public Order addOrder(Order order) {
        return save(order);
    }

    @Transactional
    @Override
    public void delOrder(Order order) {
        delete(order);
    }

    @Transactional
    @Override
    public Order findOrderById(int orderId) {
        return findObjectById(orderId);
    }

    @Transactional
    @Override
    public List<Order> findOrderByNumber(String orderNumber) {
        return findObjectsByAttributeValue(ORDER_NUMBER_ATTRIBUTE_NAME, orderNumber);
    }

    @Transactional
    @Override
    public List<Order> findAllOrders() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public List<Order> findAllOrders(String stateType) {
        return findObjectsByAttributeValue(STATE_TYPE_ATTRIBUTE_NAME, stateType);
    }

    @Transactional
    @Override
    public Order updOrderState(Order order, String stateType) {
        order.setStateType(stateType);
        getCurrentSession().update(order);

        return order;
    }
}
