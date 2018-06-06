package com.ywb.tuyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Menu;
import com.ywb.tuyue.utils.U;

import java.util.List;

/**
 * Created by mhdt on 2017/12/15.
 * 菜单适配器
 */
public class SettingMenuAdapter extends BaseRecycleViewAdapter<Menu> {
    private int checkedPosition;

    public SettingMenuAdapter(Context c, List<Menu> menuList, int checkedPosition) {
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
            rootView.setText(bean.getMenuName());
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
