package com.ywb.tuyue.ui.food;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.widget.HeaderView;

import butterknife.BindView;

/**
 * Created by mhdt on 2017/12/16.
 * 点餐
 */

public class FoodOlderActivity extends BaseActivity {
    @BindView(R.id.header)
    HeaderView header;
    String foodTag = "foodTag", olderTag = "olderTag";
    private long lastMill;

    @Override
    protected int getViewId() {
        return R.layout.activity_foodolder;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.flag_food, new FoodMenuFrag(), foodTag);
            ft.add(R.id.flag_older, new FoodOlderFrag(), olderTag);
            ft.commit();
        }
    }

    public FoodMenuFrag getFoodMenuFrag() {
        return (FoodMenuFrag) getSupportFragmentManager().findFragmentByTag(foodTag);
    }

    public FoodOlderFrag getFoodOlderFrag() {
        return (FoodOlderFrag) getSupportFragmentManager().findFragmentByTag(olderTag);
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        header.setTitleOnly(R.string.foodolder_title);
        header.setRightBtnVisiable(View.GONE);
        header.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void onDestroy() {
        header.unRegister();
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setFoodTime(lastCount.getFoodTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        super.onDestroy();
    }
}
