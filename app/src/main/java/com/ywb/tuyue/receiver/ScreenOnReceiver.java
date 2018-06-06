package com.ywb.tuyue.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ywb.tuyue.AppManager;
import com.ywb.tuyue.ui.home.UnlockedActivity;

/**
 * Created by penghao on 2018/3/14.
 * description：监听屏幕亮熄
 */

public class ScreenOnReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // 开屏
            Log.d("onReceive()", "---开屏---");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // 锁屏
            Log.d("onReceive()", "---锁屏---");
            if (!AppManager.getAppManager().currentActivity().getClass().getName().contains("UnlockedActivity")) {
                Intent i = new Intent(context, UnlockedActivity.class);
                context.startActivity(i);
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
            Log.d("onReceive()", "---解锁---");
        }

    }
}
