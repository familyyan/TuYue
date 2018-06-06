package com.ywb.tuyue.ui.setting;

import android.os.Bundle;
import android.widget.ImageView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.ui.BaseFragment;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/18.
 * description：
 */

public class AboutUsFrag extends BaseFragment {
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected int getViewId() {
        return R.layout.frag_about_us;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        imageView.setImageResource(R.drawable.icon_set);
    }
}
