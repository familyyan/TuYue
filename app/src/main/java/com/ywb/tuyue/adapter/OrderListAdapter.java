package com.ywb.tuyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.MyOrderDetail;
import com.ywb.tuyue.utils.T;

import java.util.List;

/**
 * Created by penghao on 2017/12/27.
 * description：
 */

public class OrderListAdapter extends BaseRecycleViewAdapter<MyOrderDetail> {
    final int ORDER_ICON = 1; //订单成功的icon
    final int ORDER_DETAIL = 2;//订单信息

    public OrderListAdapter(Context c, List<MyOrderDetail> mList) {
        super(c, mList);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ORDER_ICON) {
            View contentView = resIdToView(parent, R.layout.item_order_icon);
            return new MyHeaderViewHolder(contentView);
        }
        View contentView = resIdToView(parent, R.layout.item_order_list);
        return new MyViewHolder(contentView);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ORDER_ICON;
        }
        return ORDER_DETAIL;
    }

    public class MyViewHolder extends BaseViewHolder {
        TextView orderNo, time, zuoW, price;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.order_No);
            time = itemView.findViewById(R.id.order_time);
            zuoW = itemView.findViewById(R.id.order_zuoW);
            price = itemView.findViewById(R.id.order_price);
        }

        @Override
        public void onBind() {
            MyOrderDetail myOrderDetail = mList.get(position - 1);
            orderNo.setText(myOrderDetail.getOrder_code());
            time.setText(T.getDate(myOrderDetail.getCreate_at()));
            zuoW.setText(myOrderDetail.getDining_car_id());
            price.setText("￥" + myOrderDetail.getTotal_price());
        }
    }

    public class MyHeaderViewHolder extends BaseViewHolder {
        TextView textView;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.order_number_text);
        }

        @Override
        public void onBind() {
            textView.setText("当前" + mList.size() + "笔订单待配送...");
        }
    }

}
