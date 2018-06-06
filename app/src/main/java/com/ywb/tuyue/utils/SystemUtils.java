package com.ywb.tuyue.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * 系统工具类
 *
 * @author mhdt
 * @version 1.0
 * @created 2017/2/4
 */

public class SystemUtils {
    private final static String TAG = "SystemUtils";

    private SystemUtils() {
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {

        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am
                    .getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 是否在主进程
     *
     * @param context
     */
    public static final boolean isInMainProgress(Context context) {
        if (context.getPackageName().equals(getProcessName(context)))
            return true;
        return false;
    }

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static int getVersionCode(Context c) throws PackageManager.NameNotFoundException {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = c.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(
                c.getPackageName(), 0);
        Log.d(TAG, packInfo.versionCode + "___"
                + packInfo.versionName);
        return packInfo.versionCode;
    }

    public static String getVersionName(Context c) throws PackageManager.NameNotFoundException {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = c.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(
                c.getPackageName(), 0);
        Log.d(TAG, packInfo.versionCode + "___"
                + packInfo.versionName);
        return packInfo.versionName;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }
}
