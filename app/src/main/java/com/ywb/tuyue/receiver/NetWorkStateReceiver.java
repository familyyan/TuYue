package com.ywb.tuyue.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.ywb.tuyue.AppManager;

/**
 * Created by penghao on 2018/1/18.
 * description：
 */

public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("网络状态发生变化");
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                if (null != AppManager.getAppManager().currentActivity() && !AppManager.getAppManager().currentActivity().isFinishing()) {
                    AppManager.getAppManager().currentActivity().onNetWorkChange();
                }
            } else {
//                Toast.makeText(context, "网络连接已断开,请链接WIFI。", Toast.LENGTH_SHORT).show();
            }
        } else {
            //这里的就不写了，前面有写，大同小异
            System.out.println("API level 大于21");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            if (networks != null && networks.length > 0) {
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    if (networkInfo.isConnected()) {
                        if (null != AppManager.getAppManager().currentActivity() && !AppManager.getAppManager().currentActivity().isFinishing()) {
                            AppManager.getAppManager().currentActivity().onNetWorkChange();
                        }
                    }
                    sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                }
            } else {
//                Toast.makeText(context, "网络连接已断开,请链接WIFI。", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
