package com.ywb.tuyue.net;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.IGenericsSerializator;


/**
 * 返回一个Gson对象
 *
 * @author mhdt
 * @version 1.0
 * @created 2017/2/4
 */
public class GsonSerializator implements IGenericsSerializator {
    private static GsonSerializator instance;
    private Gson mGson;

    public GsonSerializator() {
        this.mGson = new Gson();
    }

    public static GsonSerializator getInstance() {
        if (instance == null) {
            synchronized (GsonSerializator.class) {
                if (instance == null) {
                    instance = new GsonSerializator();
                }
            }
        }

        return instance;
    }

    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGson.fromJson(response, classOfT);
    }


}
