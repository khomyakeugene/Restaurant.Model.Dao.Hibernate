package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.WarehouseDao;
import com.company.restaurant.dao.hibernate.proto.HDaoAmountLinkEntity;
import com.company.restaurant.model.Ingredient;
import com.company.restaurant.model.Portion;
import com.company.restaurant.model.Warehouse;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yevhen on 16.06.2016.
 */
public class HWarehouseDao extends HDaoAmountLinkEntity<Warehouse> implements WarehouseDao {
    private static final String INGREDIENT_ID_ATTRIBUTE_NAME = "ingredientId";
    private static final String PORTION_ID_ATTRIBUTE_NAME = "portionId";
    private static final String AMOUNT_ATTRIBUTE_NAME = "amount";

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
}
