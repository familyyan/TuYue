package com.ywb.tuyue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Food;
import com.ywb.tuyue.db.dao.FoodDao;
import com.ywb.tuyue.ui.food.FoodOlderActivity;

import java.util.List;

/**
 * Created by mhdt on 2017/12/18.
 * 订单适配器来自彭浩的另外台机器
 */
public class FoodOlderAdapter extends BaseRecycleViewAdapter<Food.ResultBean> {
    final int FOODOLDER = 1;
    final int FOODBALANE = 2;
    double totalPrice;
    private String TAG = "FoodOlderAdapter";

    public FoodOlderAdapter(Context c, List<Food.ResultBean> mList) {
        super(c, mList);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOODOLDER) {
            return new FoodOlderViewHolder(resIdToView(parent, R.layout.item_foodolder));
        }
        return new FoodBalanceViewHolder(resIdToView(parent, R.layout.item_foodblance));
    }

    @Override
    public int getItemCount() {
        if (super.getItemCount() > 0) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return FOODBALANE;
        }
        return FOODOLDER;
    }

    private class FoodOlderViewHolder extends BaseViewHolder {
        TextView name, price, number;
        ImageView add, jian;


        public FoodOlderViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            price = itemView.findViewById(R.id.order_price);
            number = itemView.findViewById(R.id.order_number);
            add = itemView.findViewById(R.id.add_one);
            jian = itemView.findViewById(R.id.jian_one);
        }

        @Override
        public void onBind() {
            Food.ResultBean resultBean = mList.get(position);
            name.setText(resultBean.getName());
            price.setText(resultBean.getPrice() + "");
            number.setText(resultBean.getNumber() + "");
            //增加
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num = number.getText().toString().trim();
                    int orderNumber = Integer.parseInt(num);
                    orderNumber++;
                    mList.get(position).setNumber(orderNumber);
                    FoodDao.getInstance().updateFood(mList.get(position));
                    getTotalPrice(FoodDao.getInstance().getFoodList());
                }
            });
            //减少
            jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num = number.getText().toString().trim();
                    int orderNumber = Integer.parseInt(num);
                    orderNumber--;
                    mList.get(position).setNumber(orderNumber);
                    FoodDao.getInstance().updateFood(mList.get(position));
                    if (orderNumber <= 0) {
                        FoodDao.getInstance().removeOne(mList.get(position));
                    }
                    getTotalPrice(FoodDao.getInstance().getFoodList());
                }
            });
        }
    }

    private class FoodBalanceViewHolder extends BaseViewHolder {
        TextView total;

        public FoodBalanceViewHolder(View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.price_total);
        }

        @Override
        public void onBind() {
            total.setText(totalPrice + "");
        }
    }

    public void getTotalPrice(List<Food.ResultBean> mList) {
        this.mList = mList;
        totalPrice = 0;
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                Log.d(TAG, "价格：" + mList.get(i).getPrice() + "---数量:" + mList.get(i).getNumber());
                double money = mList.get(i).getPrice() * mList.get(i).getNumber();
                totalPrice += money;
            }
        }
        notifyDataSetChanged();
        ((FoodOlderActivity) c).getFoodOlderFrag().setXiaDanBg();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
