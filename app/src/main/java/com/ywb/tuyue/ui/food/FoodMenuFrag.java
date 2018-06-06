package com.ywb.tuyue.ui.food;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.OrderListAdapter;
import com.ywb.tuyue.bean.FoodMenuBean;
import com.ywb.tuyue.bean.MyOrderDetail;
import com.ywb.tuyue.bean.MyOrderListBack;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.DeviceUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhdt on 2017/12/16.
 * 菜单浏览
 */
public class FoodMenuFrag extends BaseFragment {
    TabLayout pagerTab;
    ViewPager pager;
    TextView showNum;
    LinearLayout seeOrders;
    List<FoodScanFrag> foodFragList;
    List<FoodMenuBean.ResultBean> foodMenuTitle;
    List<MyOrderDetail> myOrderList;

    private int pageIndex = 1, pageCount = 100;

    int currentItem;

    @Override
    protected int getViewId() {
        return R.layout.frag_foodmenu;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        seeOrders = findViewById(R.id.ll_see_list);
        showNum = findViewById(R.id.show_num);
        pager = findViewById(R.id.pager);
        pagerTab = findViewById(R.id.pagerTab);
        foodFragList = new ArrayList<>();
        foodMenuTitle = new ArrayList<>();
        myOrderList = new ArrayList<>();
        getMenuListAndContent();
//        getMyOrderList();
        seeOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != dialog && dialog.isShowing()) {
                    return;
                }
                showOrderListDialog();
            }
        });
//        setViewPager();
    }

    /**
     * 获取我的订单列表
     */
    public void getMyOrderList() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(Urls.myOrderList)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams(AppConfig.page, pageIndex + "")//
                .addParams(AppConfig.pageSize, pageCount + "")//
                .addParams("imei", DeviceUtils.getUniqueId(mActivity))//PAD唯一识别码
                .build()//
                .execute(new AppGsonCallback<MyOrderListBack>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(MyOrderListBack response, int id) {
                        super.onResponseOK(response, id);
                        myOrderList.clear();
                        List<MyOrderDetail> result = response.getResult();
                        if (result != null) {
                            myOrderList.addAll(result);
                        }
                        if (myOrderList != null & !myOrderList.isEmpty()) {
//                            seeOrders.setVisibility(View.VISIBLE);
                            showNum.setText("下单成功，当前" + response.getResultNum() + "笔订单等待配送...");
                        } else {
                            seeOrders.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * 获取全部菜单列表和内容
     */
    private void getMenuListAndContent() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.listAllFood)//
                .build()//
                .execute(new AppGsonCallback<FoodMenuBean>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(FoodMenuBean response, int id) {
                        super.onResponseOK(response, id);
                        foodMenuTitle.clear();
                        List<FoodMenuBean.ResultBean> result = response.getResult();
                        if (result != null) {
                            foodMenuTitle.addAll(result);
                        }
                        pager.post(() -> setViewPager());
                    }
                });
    }

    private void setViewPager() {
        foodFragList.clear();
        for (int i = 0; i < foodMenuTitle.size(); i++) {
            Bundle bundle = new Bundle();
            FoodScanFrag scanFrag = new FoodScanFrag();
            bundle.putSerializable("foodList", foodMenuTitle.get(i));
            scanFrag.setArguments(bundle);
            foodFragList.add(scanFrag);
        }
        pager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return foodFragList.get(position);
            }

            @Override
            public int getCount() {
                return foodFragList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return foodMenuTitle.get(position).getName();
            }
        });
        //----
        for (int i = 0; i < foodMenuTitle.size(); i++) {
            if (i == currentItem) {
                pagerTab.addTab(pagerTab.newTab().setText(foodMenuTitle.get(i).getName()), true);
            } else {
                pagerTab.addTab(pagerTab.newTab().setText(foodMenuTitle.get(i).getName()));
            }

        }
        pagerTab.setupWithViewPager(pager);
    }

    AlertDialog dialog;

    /**
     * 查看配送订单
     */
    private void showOrderListDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_order_list, null);
        RecyclerView orderList = view.findViewById(R.id.rv_list);
        orderList.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        orderList.setLayoutManager(new LinearLayoutManager(mActivity));
        orderList.setAdapter(new OrderListAdapter(mActivity, myOrderList));
        dialog = new AlertDialog//
                .Builder(mActivity)//
                .setView(view)//
                .create();
        dialog.show();
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

}
