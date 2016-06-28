package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.IngredientDao;
import com.company.restaurant.dao.PortionDao;
import com.company.restaurant.dao.WarehouseDao;
import com.company.restaurant.dao.hibernate.proto.HDaoAmountLinkEntity;
import com.company.restaurant.dao.proto.SqlExpressions;
import com.company.restaurant.model.Ingredient;
import com.company.restaurant.model.Portion;
import com.company.restaurant.model.Warehouse;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 16.06.2016.
 */
public class HWarehouseDao extends HDaoAmountLinkEntity<Warehouse> implements WarehouseDao {
    private static final String INGREDIENT_ID_ATTRIBUTE_NAME = "ingredientId";
    private static final String PORTION_ID_ATTRIBUTE_NAME = "portionId";
    private static final String AMOUNT_ATTRIBUTE_NAME = "amount";
    private static final String INGREDIENT_ATTRIBUTE_NAME = "ingredient";
    private static final String SQL_ELAPSING_WAREHOUSE_INGREDIENTS =
            String.format("%s < :%s", AMOUNT_ATTRIBUTE_NAME, AMOUNT_ATTRIBUTE_NAME);

    private IngredientDao ingredientDao;
    private PortionDao portionDao;

    public void setIngredientDao(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    public void setPortionDao(PortionDao portionDao) {
        this.portionDao = portionDao;
    }

    @Override
    protected void initMetadata() {
        orderByCondition = getOrderByCondition(INGREDIENT_ID_ATTRIBUTE_NAME);
        firstIdAttributeName = INGREDIENT_ID_ATTRIBUTE_NAME;
        secondIdAttributeName = PORTION_ID_ATTRIBUTE_NAME;
        linkDataAttributeName = AMOUNT_ATTRIBUTE_NAME;
    }

    @Transactional
    @Override
    public void addIngredientToWarehouse(Ingredient ingredient, Portion portion, float amount) {
        if (amount > 0.0) {
            increaseAmount(ingredient.getIngredientId(), portion.getPortionId(), amount);
        }
    }

    @Transactional
    @Override
    public void takeIngredientFromWarehouse(Ingredient ingredient, Portion portion, float amount) {
        if (amount > 0.0) {
            decreaseAmount(ingredient.getIngredientId(), portion.getPortionId(), amount);
        }
    }

    @Transactional
    @Override
    public List<Warehouse> findIngredientInWarehouseByName(String name) {
        return findObjectsByAttributeValue(INGREDIENT_ATTRIBUTE_NAME, ingredientDao.findIngredientByName(name));
    }

    @Transactional
    @Override
    public List<Warehouse> findIngredientInWarehouseById(int ingredientId) {
        return findObjectsByAttributeValue(INGREDIENT_ATTRIBUTE_NAME, ingredientDao.findIngredientById(ingredientId));
    }

    @Transactional
    @Override
    public List<Warehouse> findAllWarehouseIngredients() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public List<Warehouse> findAllElapsingWarehouseIngredients(float limit) {
        Query<Warehouse> query = getCurrentSession().createQuery(SqlExpressions.fromExpression(
                getEntityName(), SqlExpressions.whereExpression(SQL_ELAPSING_WAREHOUSE_INGREDIENTS),
                getDefaultOrderByCondition()), Warehouse.class);
        query.setParameter(AMOUNT_ATTRIBUTE_NAME, limit);

        return query.list();
    }
}
