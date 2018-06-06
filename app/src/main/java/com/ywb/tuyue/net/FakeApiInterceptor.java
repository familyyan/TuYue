package com.ywb.tuyue.net;

import android.util.Log;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.db.DataBase;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by penghao on 2018/2/1.
 * description： 拦截器
 */

public class FakeApiInterceptor implements Interceptor {

    private static final String TAG = "FakeApiInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        String json;
        Log.d(TAG, "=====url====" + chain.request().url().toString());
        if (chain.request().url().toString().startsWith(MyUrls.getCityList)) {
            json = DataBase.getString(AppConfig.CITY_DATA);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getArticleList)) {
            json = DataBase.getString(AppConfig.GETARTICLELIST);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.homePage)) {
            json = DataBase.getString(AppConfig.HOMEPAGE);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getStdbyAdvert)) {
            json = DataBase.getString(AppConfig.GETSTDBYADVERT);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getMoviesConfigTypeList)) {
            json = DataBase.getString(AppConfig.GETMOVIESCONFIGTYPELIST);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.listAllFood)) {
            json = DataBase.getString(AppConfig.LISTALLFOOD);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getGames)) {
            json = DataBase.getString(AppConfig.GAME_DATA);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getBooks)) {
            json = DataBase.getString(AppConfig.BOOK_DATA);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else if (chain.request().url().toString().startsWith(MyUrls.getMusic)) {
            json = DataBase.getString(AppConfig.MUSIC_DATA);
            Log.d(TAG, "=====json====" + json);
            response = makeMyJson(chain, json);
        } else {
            response = chain.proceed(chain.request());
        }
        return response;
    }


    private Response makeMyJson(Chain chain, String json) {

        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message(json)
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .build();
    }
}
