package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.WarehouseDao;
import com.company.restaurant.dao.WarehouseViewDao;
import com.company.restaurant.dao.hibernate.proto.HDaoAmountLinkEntity;
import com.company.restaurant.dao.proto.SqlExpressions;
import com.company.restaurant.model.Ingredient;
import com.company.restaurant.model.Portion;
import com.company.restaurant.model.WarehouseView;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 15.06.2016.
 */
public class HWarehouseViewDao extends HDaoAmountLinkEntity<WarehouseView> implements WarehouseViewDao {
    private static final String INGREDIENT_ID_ATTRIBUTE_NAME = "ingredientId";
    private static final String PORTION_ID_ATTRIBUTE_NAME = "portionId";
    private static final String INGREDIENT_NAME_ATTRIBUTE_NAME = "ingredientName";
    private static final String AMOUNT_ATTRIBUTE_NAME = "amount";
    private static final String SQL_ELAPSING_WAREHOUSE_INGREDIENTS =
            String.format("%s < :%s", AMOUNT_ATTRIBUTE_NAME, AMOUNT_ATTRIBUTE_NAME);

    private WarehouseDao warehouseDao;

    public void setWarehouseDao(WarehouseDao warehouseDao) {
        this.warehouseDao = warehouseDao;
    }

    @Override
    protected void initMetadata() {
        orderByCondition = getOrderByCondition(INGREDIENT_NAME_ATTRIBUTE_NAME);
        firstIdAttributeName = INGREDIENT_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = PORTION_ID_ATTRIBUTE_NAME;
        linkDataAttributeName = AMOUNT_ATTRIBUTE_NAME;
    }

    @Transactional
    @Override
    public void addIngredientToWarehouse(Ingredient ingredient, Portion portion, float amount) {
        warehouseDao.addIngredientToWarehouse(ingredient, portion, amount);
    }

    @Transactional
    @Override
    public void takeIngredientFromWarehouse(Ingredient ingredient, Portion portion, float amount) {
        warehouseDao.takeIngredientFromWarehouse(ingredient, portion, amount);
    }

    @Transactional
    @Override
    public List<WarehouseView> findIngredientInWarehouseByName(String name) {
        return findObjectsByAttributeValue(INGREDIENT_NAME_ATTRIBUTE_NAME, name);
    }

    @Transactional
    @Override
    public List<WarehouseView> findIngredientInWarehouseById(int ingredientId) {
        return findObjectsByAttributeValue(INGREDIENT_ID_ATTRIBUTE_NAME, ingredientId);
    }

    @Transactional
    @Override
    public List<WarehouseView> findAllWarehouseIngredients() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public List<WarehouseView> findAllElapsingWarehouseIngredients(float limit) {
        Query<WarehouseView> query = getCurrentSession().createQuery(SqlExpressions.fromExpression(
                getEntityName(), SqlExpressions.whereExpression(SQL_ELAPSING_WAREHOUSE_INGREDIENTS),
                getDefaultOrderByCondition()), getEntityClass());
        query.setParameter(AMOUNT_ATTRIBUTE_NAME, limit);

        return query.list();
    }
}
