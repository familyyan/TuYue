package com.ywb.tuyue.db.dao;


import com.simple.util.db.operation.TemplateDAO;
import com.ywb.tuyue.AppContext;
import com.ywb.tuyue.bean.Food;
import com.ywb.tuyue.db.DBHelper;

import java.util.List;

/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by:
 */
public class FoodDao extends TemplateDAO<Food.ResultBean> {
    private static FoodDao instance;

    public FoodDao() {
        super(new DBHelper(AppContext.getApplication()));
    }

    public static FoodDao getInstance() {
        if (instance == null) {
            synchronized (FoodDao.class) {
                if (instance == null) {
                    instance = new FoodDao();
                }
            }
        }
        return instance;
    }

    public List<Food.ResultBean> getFoodList() {
        List<Food.ResultBean> foods = find();
        if (foods != null && foods.size() > 0) {
            return foods;
        } else {
            return null;
        }
    }

    public void removeOne(Food.ResultBean resultBean) {
        delete(resultBean.getId());
    }

    public void updateFood(Food.ResultBean food) {
        if (get(food.getId()) == null) {
            insert(food);
        } else {
            update(food);
        }

    }

}
