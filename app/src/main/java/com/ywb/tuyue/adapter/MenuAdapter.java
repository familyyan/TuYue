package com.ywb.tuyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.MovieMenuBean;
import com.ywb.tuyue.utils.U;

import java.util.List;

/**
 * Created by mhdt on 2017/12/15.
 * 菜单适配器
 */
public class MenuAdapter extends BaseRecycleViewAdapter<MovieMenuBean.ResultBean> {
    private int checkedPosition;

    public MenuAdapter(Context c, List<MovieMenuBean.ResultBean> menuList, int checkedPosition) {
        super(c, menuList);
        this.checkedPosition = checkedPosition;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHodler(resIdToView(parent, R.layout.item_menu));
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    private class MenuViewHodler extends BaseViewHolder<TextView> {
        public MenuViewHodler(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind() {
            rootView.setText(bean.getName());
            if (checkedPosition == position) {
                rootView.setTextColor(U.getColor(R.color.menu_text_checked));
                rootView.setBackgroundColor(U.getColor(R.color.menu_checked));
            } else {
                rootView.setTextColor(U.getColor(R.color.menu_text_normal));
                rootView.setBackgroundColor(U.getColor(R.color.transport));
            }
        }
    }
}
