package com.ywb.tuyue.ui.subway;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.SuburbanAdapter;
import com.ywb.tuyue.bean.ChengTie;
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
 * description: 城铁风采
 */

public class SuburbanFragment extends BaseFragment {

    RecyclerView recyclerview;
    SmartRefreshLayout refreshLayout;
    LinearLayout emptyView;

    List<ChengTie.ResultBean> suburbanList = new ArrayList<>();
    private SuburbanAdapter suburbanAdapter;
    private int page = 1, pageSize = 100;

    @Override
    protected int getViewId() {
        return R.layout.frag_suburban;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerview = findViewById(R.id.RecyclerView);
        refreshLayout = findViewById(R.id.RefreshLayout);
        emptyView = findViewById(R.id.empty_view);
        recyclerview.post(() -> {
            int height = recyclerview.getHeight();
            recyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerview.setHasFixedSize(true);
            suburbanAdapter = new SuburbanAdapter(mActivity, suburbanList, height / 2.8f);
            recyclerview.setAdapter(suburbanAdapter);

            ClassicsHeader classicsHeader = new ClassicsHeader(mActivity);
            classicsHeader.getTitleText().setTextColor(U.getColor(R.color.white));
            classicsHeader.getLastUpdateText().setTextColor(U.getColor(R.color.white));
            refreshLayout.setRefreshHeader(classicsHeader);
            refreshLayout.setEnableLoadmore(false);
            refreshLayout.setEnableRefresh(true);

            refreshLayout.setOnRefreshListener(refreshlayout -> {
                refreshLayout.finishRefresh();
            });
            refreshLayout.setOnLoadmoreListener(refreshlayout -> loadData());
            loadData();
            refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
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
                .url(MyUrls.getArticleList)
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams(AppConfig.page, page + "")//
                .addParams(AppConfig.pageSize, pageSize + "")//
                .addParams("typeId", "2")//假数据
                .build()//
                .execute(new AppGsonCallback<ChengTie>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(ChengTie response, int id) {
                        super.onResponseOK(response, id);
                        List<ChengTie.ResultBean> result = response.getResult();
                        if (result != null && !result.isEmpty()) {
                            setEmptyViewShow(false);
                        } else {
                            setEmptyViewShow(true);
                        }
                        suburbanList.clear();
                        suburbanList.addAll(result);
                        if (suburbanAdapter != null) {
                            suburbanAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        refreshLayout.finishRefresh();
                    }
                });
    }
}
