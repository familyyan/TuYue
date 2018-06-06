package com.ywb.tuyue.ui.city;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.HTMLFormat;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/22.
 * description： 城市详情
 */

public class CityDetailsFragment extends BaseFragment {
    private static final String TAG = "CityDetailsFragment";
    City.ResultBean cityBean;
    @BindView(R.id.city_d_name)
    TextView cityDName;
    @BindView(R.id.city_d_pingY)
    TextView cityDPingY;
    @BindView(R.id.webview)
    WebView webview;


    @Override
    protected int getViewId() {
        return R.layout.frag_city_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = mActivity.getIntent();
        cityBean = (City.ResultBean) intent.getSerializableExtra("cityBean");
        setUi();
    }

    private void setUi() {
        cityDName.setText(cityBean.getName());
        if (cityBean.getPinyin() != null) {
            cityDPingY.setText(cityBean.getPinyin());
        }
        webview.getSettings().setJavaScriptEnabled(true);//启用js
        webview.getSettings().setBlockNetworkImage(false);//解决图片不显示
        String content;
        if (null != cityBean.getContentPd()) {
            Log.d(TAG, "content: " + cityBean.getContentPd());
            content = cityBean.getContentPd();
            webview.loadDataWithBaseURL(null, HTMLFormat.getNewContent(content), "text/html", "utf-8", null);
//            RichText.from(cityBean.getContent()).into(cityDContent);
        }
//        String[] split = new String[0];
//        if (cityBean.getContent_img() != null) {
//            split = cityBean.getContent_img().split(",");
//        }
//        if (split.length > 1) {//只能有2张图片
//            LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(split[0]), cityDImage01);
//            LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(split[1]), cityDImage02);
//        }
    }
}
