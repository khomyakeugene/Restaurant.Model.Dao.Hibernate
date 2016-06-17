package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.IngredientDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntitySimpleDic;
import com.company.restaurant.model.Ingredient;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 15.06.2016.
 */
public class HIngredientDao extends HDaoEntitySimpleDic<Ingredient> implements IngredientDao {
    @Transactional
    @Override
    public Ingredient addIngredient(String name) {
        return save(name);
    }

    @Transactional
    @Override
    public void delIngredient(String name) {
        delete(name);
    }

    @Transactional
    @Override
    public List<Ingredient> findAllIngredients() {
        return findAllObjects();
    }

    @Transactional
    @Override
    public Ingredient findIngredientById(int ingredientId) {
        return findObjectById(ingredientId);
    }
}
