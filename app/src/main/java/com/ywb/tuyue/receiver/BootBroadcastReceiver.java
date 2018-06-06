package com.ywb.tuyue.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.db.DataBase;

/**
 * Created by penghao on 2018/5/15.
 * description：
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";

    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_BOOT.equals(action)) {
            // 开机
            Log.d(TAG, "---开机---");
            int anInt = DataBase.getInt(AppConfig.startCountTest);
            DataBase.saveInt(AppConfig.startCountTest, anInt + 1);

        } else if (ACTION_SHUTDOWN.equals(action)) {
            // 关机
            Log.d(TAG, "---关机---");

        }
    }
}
