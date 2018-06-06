package com.ywb.tuyue.ui.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.CityAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.U;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by penghao on 2017/12/22.
 * description：城市列表
 */

public class CityFragment extends BaseFragment {

    RecyclerView recyclerview;
    SmartRefreshLayout refreshLayout;
    LinearLayout emptyView;
    List<City.ResultBean> cityList;
    private CityAdapter cityAdapter;

    private int page = 1, pageSize = 100;

    @Override
    protected int getViewId() {
        return R.layout.frag_city;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerview = findViewById(R.id.cityRecyclerView);
        refreshLayout = findViewById(R.id.cityRefreshLayout);
        emptyView = findViewById(R.id.empty_view);
        cityList = new ArrayList<>();

        recyclerview.post(new Runnable() {
            @Override
            public void run() {
                int height = recyclerview.getHeight();
                recyclerview.setLayoutManager(new GridLayoutManager(mActivity, 3));
                recyclerview.setHasFixedSize(true);
                recyclerview.setAdapter(cityAdapter = new CityAdapter(mActivity, cityList, height / 2.5f));
                cityAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int pos) {
                        //---详情
                        Intent intent = new Intent();
                        intent.putExtra("cityBean", cityList.get(pos));
                        CityDetailsActivity.Go(mActivity, CityDetailsActivity.class, intent);
                    }
                });
                ClassicsHeader classicsHeader = new ClassicsHeader(mActivity);
                classicsHeader.getTitleText().setTextColor(U.getColor(R.color.white));
                classicsHeader.getLastUpdateText().setTextColor(U.getColor(R.color.white));
                refreshLayout.setRefreshHeader(classicsHeader);

                refreshLayout.setEnableLoadmore(false);
                refreshLayout.setEnableRefresh(true);

                refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        refreshLayout.finishRefresh();
                    }
                });
                loadData();
                refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
            }
        });

        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    private void setEmptyViewShow(boolean isShow) {
        emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        OkHttpUtils
                .get()//
                .tag(this)//
                .url(MyUrls.getCityList)
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams(AppConfig.page, page + "")//
                .addParams(AppConfig.pageSize, pageSize + "")//
                .build()//
                .execute(new AppGsonCallback<City>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(City response, int id) {
                        super.onResponseOK(response, id);
                        List<City.ResultBean> result = response.getResult();
                        if (result != null && !result.isEmpty()) {
                            setEmptyViewShow(false);
                        } else {
                            setEmptyViewShow(true);
                        }
                        cityList.clear();
                        cityList.addAll(result);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        refreshLayout.finishRefresh();
                        cityAdapter.notifyDataSetChanged();
                    }
                });
    }
}
