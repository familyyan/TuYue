package com.ywb.tuyue.db;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by mc_luo on 2017/9/28.
 */
public class SharedPreferenceHelper {
    private static Application sApplication;
    public static void init(Application application) {
        sApplication = application;
    }

    public static SharedPreferences getSharePreference() {
        return PreferenceManager.getDefaultSharedPreferences(sApplication);
    }

    // boolean数据
    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = getSharePreference().edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key) {
        return getSharePreference().getBoolean(key, false);
    }

    // int数据
    public static void saveInt(String key, int value) {
        SharedPreferences.Editor edit = getSharePreference().edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key) {
        return getSharePreference().getInt(key, 0);
    }

    public static int getInt1(String key) {
        return getSharePreference().getInt(key, -1);
    }

    // long数据
    public static void saveLong(String key, long value) {
        SharedPreferences.Editor edit = getSharePreference().edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key) {
        return getSharePreference().getLong(key, 0);
    }

    // String数据
    public static void saveString(String key, String value) {
        SharedPreferences.Editor edit = getSharePreference().edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(String key) {
        return getSharePreference().getString(key, "");
    }

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param fileKey    储存文件的key
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void save(String fileKey, String key, Object saveObject) {
        SharedPreferences.Editor editor = getSharePreference().edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param fileKey 储存文件的key
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object get(String fileKey, String key) {
        String string = getSharePreference().getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

    //-----
    //倒计时
    public static void saveDjs(long time) {
        saveLong("djs", time);
    }

    public static long getDjs() {
        return getLong("djs");
    }

    //新增客户时的缓存数据
    public static void saveCustomer(Object customer) {
        save("customer", "customer", customer);
    }

    public static Object getCustomer() {
        return get("customer", "customer");
    }
}
