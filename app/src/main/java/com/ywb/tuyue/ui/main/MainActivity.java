package com.ywb.tuyue.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.receiver.ScreenOnReceiver;
import com.ywb.tuyue.service.CountService;
import com.ywb.tuyue.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private String mainFrag = "mainFrag";
    MainFragment mainFragment;
    private ScreenOnReceiver screenOnReceiver;


    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_view, mainFragment, mainFrag).commit();
        }

        //监听屏幕亮熄
        if (screenOnReceiver == null) {
            screenOnReceiver = new ScreenOnReceiver();
        }
        Log.d("##开机次数##", "initView: " + DataBase.getInt(AppConfig.startCountTest));
        registerScreenBroadcastReceiver();
        startSendCountData();
    }

    /**
     * 开始发送统计数据
     */
    private void startSendCountData() {
        Intent intent = new Intent(mContext, CountService.class);
        startService(intent);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onNetWorkConnection() {
        super.onNetWorkConnection();
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(mainFrag);
        mainFragment.getData();
    }


    public void registerScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnReceiver, filter);
    }

    public void unRegisterScreenBroadcastReceiver() {
        unregisterReceiver(screenOnReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterScreenBroadcastReceiver();
    }

}
