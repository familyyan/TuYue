package com.ywb.tuyue.ui.city;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.widget.HeaderView;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/22.
 * description： 城市详情
 */

public class CityDetailsActivity extends BaseActivity {

    @BindView(R.id.title)
    HeaderView title;
    City.ResultBean cityBean;

    @Override
    protected int getViewId() {
        return R.layout.activity_city_details;
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        Intent intent = getIntent();
        cityBean = (City.ResultBean) intent.getSerializableExtra("cityBean");
        title.setTitle(cityBean.getName());
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            CityDetailsFragment cityDetailsFragment = new CityDetailsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.replace, cityDetailsFragment, CityFragment.class.getClass().getName()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        super.onDestroy();
    }
}
