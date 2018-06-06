package com.ywb.tuyue.ui.setting;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.WifiListAdapter;
import com.ywb.tuyue.bean.WifiBean;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.CollectionUtils;
import com.ywb.tuyue.utils.WifiSupport;
import com.ywb.tuyue.widget.WifiLinkDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by penghao on 2018/1/6.
 * description：
 */

public class WIFIsettingFragment extends BaseFragment {

    private static final String TAG = "MainActivity";
    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean mHasPermission;

    List<WifiBean> realWifiList = new ArrayList<>();

    private WifiListAdapter adapter;

    private RecyclerView recyWifiList;

    private WifiBroadcastReceiver wifiReceiver;

    private SwitchButton switch_button;

    private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）


    @Override
    public void onResume() {
        super.onResume();
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        mActivity.registerReceiver(wifiReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.unregisterReceiver(wifiReceiver);
    }

    @Override
    protected int getViewId() {
        return R.layout.frag_wifi_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        switch_button = findViewById(R.id.switch_button);
        initRecycler();
        mHasPermission = checkPermission();
        if (!mHasPermission && WifiSupport.isOpenWifi(mActivity)) {  //未获取权限，申请权限
            requestPermission();
        } else if (mHasPermission && WifiSupport.isOpenWifi(mActivity)) {  //已经获取权限
//            initRecycler();
            switch_button.setChecked(true);
        } else {
            Toast.makeText(mActivity, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
//            switch_button.setChecked(false);
        }

        switch_button.setChecked(DataBase.getBoolean(AppConfig.switch_btn_state));

        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                setWifiEnable(isChecked);
            }
        });
    }

    /**
     * 关闭或者打开wifi
     *
     * @param state
     */
    public void setWifiEnable(boolean state) {
        //首先，用Context通过getSystemService获取wifimanager
        WifiManager mWifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //调用WifiManager的setWifiEnabled方法设置wifi的打开或者关闭，只需把下面的state改为布尔值即可（true:打开 false:关闭）
        mWifiManager.setWifiEnabled(state);
    }

    private void initRecycler() {
        recyWifiList = findViewById(R.id.recy_list_wifi);
        adapter = new WifiListAdapter(mActivity, realWifiList);
        recyWifiList.setLayoutManager(new LinearLayoutManager(mActivity));
        recyWifiList.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        recyWifiList.setAdapter(adapter);

        if (WifiSupport.isOpenWifi(mActivity) && mHasPermission) {
            sortScaResult();
        } else {
//            Toast.makeText(mActivity, "WIFI处于关闭状态或权限获取失败", Toast.LENGTH_SHORT).show();
        }

        adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Object o) {
                WifiBean wifiBean = realWifiList.get(postion);
                if (wifiBean.getState().equals(AppConfig.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppConfig.WIFI_STATE_CONNECT)) {
                    String capabilities = realWifiList.get(postion).getCapabilities();
                    if (WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {//无需密码
                        WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), mActivity);
                        if (tempConfig == null) {
                            WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                            WifiSupport.addNetWork(exsits, mActivity);
                        } else {
                            WifiSupport.addNetWork(tempConfig, mActivity);
                        }
                    } else {   //需要密码，弹出输入密码dialog
                        noConfigurationWifi(postion);
                    }
                }
            }
        });
    }


    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.d(TAG, "已经关闭");
                        Toast.makeText(mActivity, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
                        sortScaResult();
                        switch_button.setChecked(false);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        Log.d(TAG, "正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        Log.d(TAG, "已经打开");
                        sortScaResult();
                        switch_button.setChecked(true);
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        Log.d(TAG, "正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        Log.d(TAG, "未知状态");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.d(TAG, "wifi没连接上");
                    for (int i = 0; i < realWifiList.size(); i++) {//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(AppConfig.WIFI_STATE_UNCONNECT);
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    Log.d(TAG, "wifi连接上了");
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(mActivity);

                    //连接成功 跳转界面 传递ip地址
//                    Toast.makeText(mActivity, "wifi连接上了", Toast.LENGTH_SHORT).show();
                    connectType = 1;
                    wifiListSet(connectedWifiInfo.getSSID(), connectType);
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    Log.d(TAG, "wifi正在连接");
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(mActivity);
                    connectType = 2;
                    wifiListSet(connectedWifiInfo.getSSID(), connectType);
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.d(TAG, "网络列表变化了");
                wifiListChange();
            }
        }
    }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     *
     * @param wifiName
     * @param type
     */
    public void wifiListSet(String wifiName, int type) {
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if (CollectionUtils.isNullOrEmpty(realWifiList)) {
            return;
        }
        for (int i = 0; i < realWifiList.size(); i++) {
            realWifiList.get(i).setState(AppConfig.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for (int i = 0; i < realWifiList.size(); i++) {
            WifiBean wifiBean = realWifiList.get(i);
            if (index == -1 && ("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)) {
                index = i;
                wifiInfo.setLevel(wifiBean.getLevel());
                wifiInfo.setWifiName(wifiBean.getWifiName());
                wifiInfo.setCapabilities(wifiBean.getCapabilities());
                if (type == 1) {
                    wifiInfo.setState(AppConfig.WIFI_STATE_CONNECT);
                } else {
                    wifiInfo.setState(AppConfig.WIFI_STATE_ON_CONNECTING);
                }
            }
        }
        if (index != -1) {
            realWifiList.remove(index);
            realWifiList.add(0, wifiInfo);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * //网络状态发生改变 调用此方法！
     */
    public void wifiListChange() {
        sortScaResult();
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(mActivity);
        if (connectedWifiInfo != null) {
            wifiListSet(connectedWifiInfo.getSSID(), connectType);
        }
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    public void sortScaResult() {
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(mActivity));
        realWifiList.clear();
        if (!CollectionUtils.isNullOrEmpty(scanResults)) {
            for (int i = 0; i < scanResults.size(); i++) {
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(AppConfig.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level) + "");
                realWifiList.add(wifiBean);
            }
        }
        //排序
        Collections.sort(realWifiList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
        WifiLinkDialog linkDialog = new WifiLinkDialog(mActivity, R.style.dialog_download, realWifiList.get(position).getWifiName(), realWifiList.get(position).getCapabilities());
        if (!linkDialog.isShowing()) {
            linkDialog.show();
        }
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(mActivity,
                NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * 检查是否已经授予权限
     *
     * @return
     */
    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;   //判断用户是否同意获取权限
                    break;
                }
            }

            //如果同意权限
            if (hasAllPermission) {
                mHasPermission = true;
                if (WifiSupport.isOpenWifi(mActivity) && mHasPermission) {  //如果wifi开关是开 并且 已经获取权限
//                    initRecycler();
                    switch_button.setChecked(true);
                } else {
                    Toast.makeText(mActivity, "WIFI处于关闭状态或权限获取失败", Toast.LENGTH_SHORT).show();
                }

            } else {  //用户不同意权限
                mHasPermission = false;
                Toast.makeText(mActivity, "获取权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        DataBase.saveBoolean(AppConfig.switch_btn_state, switch_button.isChecked());
        super.onDestroy();
    }
}
