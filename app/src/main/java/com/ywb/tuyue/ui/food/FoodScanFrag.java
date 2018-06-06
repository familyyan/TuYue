package com.ywb.tuyue.ui.food;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.FoodScanAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.FoodMenuBean;
import com.ywb.tuyue.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhdt on 2017/12/18.
 * 菜单浏览
 */

public class FoodScanFrag extends BaseFragment {
    RecyclerView recyclerview;
    SmartRefreshLayout refreshLayout;

    List<FoodMenuBean.ResultBean.FoodListBean> foodList = new ArrayList<>();
    FoodScanAdapter foodScanAdapter;

    private FoodMenuBean.ResultBean foodListBean;


    @Override
    protected int getViewId() {
        return R.layout.frag_food;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        emptyView = findViewById(R.id.empty_view);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerview = findViewById(R.id.recyclerview);

        foodListBean = (FoodMenuBean.ResultBean) getArguments().getSerializable("foodList");
        if (foodListBean != null) {
            foodList.clear();
            List<FoodMenuBean.ResultBean.FoodListBean> localFoodList = foodListBean.getFoodList();
            foodList.addAll(localFoodList);
            setEmptyViewShow(false);
        } else {
            setEmptyViewShow(true);
        }


        recyclerview.post(new Runnable() {
            @Override
            public void run() {
                int height = recyclerview.getHeight();
                recyclerview.setLayoutManager(new GridLayoutManager(mActivity, 4));
                recyclerview.setHasFixedSize(true);
                recyclerview.setAdapter(foodScanAdapter = new FoodScanAdapter(mActivity, FoodScanFrag.this.foodList, height / 2));
                foodScanAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int pos) {

                    }
                });
                //---------------
                ClassicsHeader classicsHeader = new ClassicsHeader(mActivity);
                ClassicsFooter classicsFooter = new ClassicsFooter(mActivity);
                refreshLayout.setRefreshHeader(classicsHeader);
                refreshLayout.setRefreshFooter(classicsFooter);
                refreshLayout.setEnableLoadmore(false);
                refreshLayout.setEnableRefresh(true);

                refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        foodScanAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                });

                refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {
                        foodScanAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadmore();
                    }
                });
                refreshLayout.autoRefresh();
                refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
            }
        });
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodScanAdapter.notifyDataSetChanged();
            }
        });
    }

    LinearLayout emptyView;

    private void setEmptyViewShow(boolean isShow) {
        emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

//    private void loadData() {
//        Observable.create(new ObservableOnSubscribe<List<Food.ResultBean>>() {
//            @Override
//            public void subscribe(final ObservableEmitter<List<Food.ResultBean>> e) throws Exception {
//                OkHttpUtils//
//                        .get()//
//                        .tag(this)//
//                        .url(Urls.listFoodByType)//
//                        .addParams(AppConfig.token_key, AppConfig.token_value)//
//                        .addParams(AppConfig.requester_key, AppConfig.requester_value)//
//                        .addParams(AppConfig.page, pageIndex + "")//
//                        .addParams(AppConfig.pageSize, pageCount + "")//
////                        .addParams("typeId", type + "")//
//                        .build()//
//                        .execute(new AppGsonCallback<Food>(new RequestModel(mActivity)) {
//                            @Override
//                            public void onResponseOK(Food response, int id) {
//                                super.onResponseOK(response, id);
//                                List<Food.ResultBean> result = response.getResult();
//                                if (result != null && !result.isEmpty()) {
//                                    setEmptyViewShow(false);
//                                } else {
//                                    if (pageIndex == 1) {
//                                        setEmptyViewShow(true);
//                                    }
//                                }
//                                pageIndex++;
//                                e.onNext(result);
//                            }
//
//
//                        });
//
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Food.ResultBean>>() {
//                    @Override
//                    public void accept(List<Food.ResultBean> temoFoodList) throws Exception {
//                        if (pageIndex == 2) {//refresh
//                            foodList.clear();
//                            refreshLayout.finishRefresh();
//                        } else {//loadmore
//                            refreshLayout.finishLoadmore();
//                        }
//                        foodList.addAll(temoFoodList);
//                        foodScanAdapter.notifyDataSetChanged();
//                        if (temoFoodList.size() >= pageCount) {
//                            refreshLayout.setEnableLoadmore(true);
//                        } else {
//                            refreshLayout.setEnableLoadmore(false);
//                        }
//                    }
//                });
//    }
}
