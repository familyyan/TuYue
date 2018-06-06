package com.ywb.tuyue.ui.subway;

import android.os.Bundle;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.widget.HeaderView;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/22.
 * description： 城铁风采
 */

public class SuburbanStyleActivity extends BaseActivity {
    @BindView(R.id.title)
    HeaderView title;
    private long lastMill;

    @Override
    protected int getViewId() {
        return R.layout.activity_suburban;
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(U.getString(R.string.sub_name));
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();
        if (savedInstanceState == null) {
            SuburbanFragment suburbanFragment = new SuburbanFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frag_sub, suburbanFragment, suburbanFragment.getClass().getName()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setSubwayTime(lastCount.getSubwayTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        super.onDestroy();
    }
}
