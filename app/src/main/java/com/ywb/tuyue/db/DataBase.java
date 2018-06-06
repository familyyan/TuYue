package com.ywb.tuyue.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.AppContext;


/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by:
 */
public class DataBase {
    public final static String SHAPENAME = AppConfig.APP_SHAPENAME;

    // boolean数据
    public static void saveBoolean(String key, boolean value) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, 0);
        return sp.getBoolean(key, false);
    }

    // int数据
    public static void saveInt(String key, int value) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, 0);
        return sp.getInt(key, 0);
    }

    // long数据
    public static void saveLong(String key, long value) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, 0);
        return sp.getLong(key, 0);
    }

    // String数据
    public static void saveString(String key, String value) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(String key) {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, 0);
        return sp.getString(key, "");
    }

    public static void clearDataBase() {
        SharedPreferences sp = AppContext.getApplication()
                .getSharedPreferences(SHAPENAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
