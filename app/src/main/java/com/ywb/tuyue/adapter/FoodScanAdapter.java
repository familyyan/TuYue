package com.ywb.tuyue.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.FoodMenuBean;
import com.ywb.tuyue.ui.food.OnAddOneClickListener;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.DensityUtil;
import com.ywb.tuyue.widget.CardFood;

import java.util.List;

/**
 * Created by mhdt on 2017/12/18.
 * 菜单浏览
 */
public class FoodScanAdapter extends BaseRecycleViewAdapter<FoodMenuBean.ResultBean.FoodListBean> {
    private int itemHeight;

    public FoodScanAdapter(Context c, List<FoodMenuBean.ResultBean.FoodListBean> mList, int height) {
        super(c, mList);
        this.itemHeight = height - DensityUtil.dip2px(10);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = resIdToView(parent, R.layout.item_foodscan);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = itemHeight;
        view.setLayoutParams(params);
        return new FoodScanViewHodler(view);
    }

    private class FoodScanViewHodler extends BaseViewHolder<CardFood> {
        CardFood cardFood;

        public FoodScanViewHodler(View itemView) {
            super(itemView);
            cardFood = itemView.findViewById(R.id.rootView);
        }

        @Override
        public void onBind() {
            cardFood.setImage(mList.get(position).getUrl());
            cardFood.setName(mList.get(position).getName());
            cardFood.setMiaoshu(mList.get(position).getDescription());
            cardFood.setPrice(mList.get(position).getPrice() + "");
            //点击添加菜品
            cardFood.setOnAddOneClickListener(new OnAddOneClickListener() {
                @Override
                public void onAddOneClickListener() {
                    if (DateUtils.isBetweenTime()) {//服务时间09:00-18:00
                        FoodMenuBean.ResultBean.FoodListBean resultBean = mList.get(position);
                        Intent intent = new Intent("send_my_order");
                        intent.putExtra("order", resultBean);
                        c.sendBroadcast(intent);
                    } else {
                        showNotInBusinessHoursDialog();
                    }
                }
            });
        }
    }

    /**
     * 不在服务时间c
     */
    private void showNotInBusinessHoursDialog() {
        View view = LayoutInflater.from(c).inflate(R.layout.dialog_not_in, null);
        new AlertDialog.Builder(c).setView(view).create().show();
    }


}
