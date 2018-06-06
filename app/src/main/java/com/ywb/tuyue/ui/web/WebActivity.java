package com.ywb.tuyue.ui.web;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.HTMLFormat;
import com.ywb.tuyue.widget.HeaderView;

import butterknife.BindView;

/**
 * Created by penghao on 2017/12/18.
 * description： 加载网络activity
 */

public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity-->";
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.webview)
    WebView webview;

    private String titleStr, contentStr;

    long lastMill;
    String type;

    @Override
    protected int getViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();

        titleStr = getIntent().getExtras().getString("title");
        contentStr = getIntent().getExtras().getString("content");
        type = getIntent().getExtras().getString("type");

        webview.getSettings().setJavaScriptEnabled(true);//启用js
        webview.getSettings().setBlockNetworkImage(false);//解决图片不显示


        if (!TextUtils.isEmpty(contentStr)) {
//            content.setText(Html.fromHtml(contentStr));
            Log.d(TAG, "contentStr: " + contentStr);
            webview.loadDataWithBaseURL(null, HTMLFormat.getNewContent(contentStr), "text/html", "utf-8", null);
//            webview.loadDataWithBaseURL(null, contentStr, "text/html", "utf-8", null);
        }
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(titleStr);
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            switch (type) {
                case "home1":
                    lastCount.setHomeAd1Time(lastCount.getHomeAd1Time() + staySecond);
                    break;
                case "home2":
                    lastCount.setHomeAd2Time(lastCount.getHomeAd2Time() + staySecond);
                    break;
                case "game":
                    break;
                case "book":
                    break;
            }
            CountDao.getInstance().updateCount(lastCount);
        }

        super.onDestroy();
    }
}
