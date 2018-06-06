package com.ywb.tuyue.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ywb.tuyue.AppManager;
import com.ywb.tuyue.ui.setting.DataSynchronizationFrag;

import java.util.TimerTask;

/**
 * Created by ph on 18-1-19.
 */

public class MyTask extends TimerTask {
    private static final String TAG = "MyTask";

    private DataSynchronizationFrag dataSynchronizationFrag;

    public MyTask(DataSynchronizationFrag dataSynchronizationFrag) {
        this.dataSynchronizationFrag = dataSynchronizationFrag;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: 执行了操作" + AppManager.getAppManager().currentActivity().getClass().getName());
        if (AppManager.getAppManager().currentActivity().getClass().getName().contains("SettingActivity")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    dataSynchronizationFrag.startSyncData(DataSynchronizationFrag.YES);
                }
            });

        } else {
            dataSynchronizationFrag.startSyncData(DataSynchronizationFrag.NO);
        }
    }
}
