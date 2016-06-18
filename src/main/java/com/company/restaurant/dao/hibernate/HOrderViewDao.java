package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.OrderDao;
import com.company.restaurant.dao.OrderViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntity;
import com.company.restaurant.model.Order;
import com.company.restaurant.model.OrderView;
import com.company.restaurant.util.ObjectService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 13.06.2016.
 */
public class HOrderViewDao extends HDaoEntity<OrderView> implements OrderViewDao {
    private static final String STATE_TYPE_ATTRIBUTE_NAME = "stateType";
    private static final String ORDER_NUMBER_ATTRIBUTE_NAME = "orderNumber";

    private OrderDao orderDao;

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    protected void initMetadata() {

    }
    @Override
    public String orderEntityName() {
        return orderDao.orderEntityName();
    }

    @Transactional
    @Override
    public OrderView addOrder(OrderView orderView) {
        Order order = orderDao.addOrder((Order) ObjectService.copyObjectByAccessors(orderView, new Order()));

        return findObjectById(order.getOrderId());
    }

    @Transactional
    @Override
    public void delOrder(OrderView orderView) {
        orderDao.delOrder((Order) ObjectService.copyObjectByAccessors(orderView, new Order()));
    }

    @Transactional
    @Override
    public OrderView findOrderById(int orderId) {
        return findObjectById(orderId);
    }

    @Transactional
    @Override
    public List<OrderView> findOrderByNumber(String orderNumber) {
        return findObjectsByAttributeValue(ORDER_NUMBER_ATTRIBUTE_NAME, orderNumber);
    }

    @Transactional
    @Override
    public List<OrderView> findAllOrders() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public List<OrderView> findAllOrders(String stateType) {
        return findObjectsByAttributeValue(STATE_TYPE_ATTRIBUTE_NAME, stateType);
    }

    @Transactional
    @Override
    public OrderView updOrderState(OrderView orderView, String stateType) {
        Order order = orderDao.updOrderState((Order) ObjectService.copyObjectByAccessors(orderView, new Order()),
                stateType);

        return (OrderView)ObjectService.copyObjectByAccessors(order, orderView);
    }
}
