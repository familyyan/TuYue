package com.ywb.tuyue.ui.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.adapter.SettingMenuAdapter;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.T;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.widget.CustomViewPager;
import com.ywb.tuyue.widget.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/18.
 * description：设置
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.setting_menu)
    RecyclerView settingMenu;
    @BindView(R.id.setting_pager)
    CustomViewPager settingPager;

    private SettingMenuAdapter menuAdapter;
    private int currentPosition;
    private List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected int getViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initMenu();
        initPager();
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitleOnly(R.string.setting_title);
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    private void initMenu() {
        menuAdapter = new SettingMenuAdapter(mContext, T.getMenu(T.arrayTList(U.getStringArray(R.array.setting_menu))), currentPosition);
        settingMenu.setLayoutManager(new LinearLayoutManager(mContext));
        //requestLayout()是很昂贵的,因为他会要求重新布局，重新绘制（详细请看Android优化），所以如当不是瀑布流时，设置这个可以避免重复的增删造成而外的浪费资源
        settingMenu.setHasFixedSize(true);
        settingMenu.setAdapter(menuAdapter);
        menuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                currentPosition = pos;
                menuAdapter.setCheckedPosition(currentPosition);
                settingPager.setCurrentPage(currentPosition);
                ((IPchangeFragment) fragments.get(3)).dismiss(itemView);
            }
        });
    }

    private void initPager() {
        fragments.add(new DataSynchronizationFrag());
        fragments.add(new SystemVersionFrag());
        fragments.add(new WIFIsettingFragment());
        fragments.add(new IPchangeFragment());
        fragments.add(new HotSpotFragment());
        fragments.add(new AboutUsFrag());
        settingPager.setScanScroll(false);
        settingPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        super.onDestroy();
    }
}
