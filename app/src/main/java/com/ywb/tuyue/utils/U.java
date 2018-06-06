package com.ywb.tuyue.utils;

import android.content.Context;
import android.content.res.Resources;

import com.ywb.tuyue.AppContext;

/**
 * Created by mhdt on 2017/12/16.
 * ui相关数据获取
 */

public class U {
    public static Context getAppContext() {
        return AppContext.getApplication();
    }

    public static Resources getResources() {
        return getAppContext().getResources();
    }

    public static int getColor(int resColor) {
        return getResources().getColor(resColor);
    }

    public static String getString(int resString) {
        return getResources().getString(resString);
    }

    public static String[] getStringArray(int resString) {
        return getResources().getStringArray(resString);
    }

}
