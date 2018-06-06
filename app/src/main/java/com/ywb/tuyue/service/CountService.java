package com.ywb.tuyue.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.bean.BaseBean;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.net.GsonSerializator;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.utils.DeviceUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by penghao on 2018/5/10.
 * description： 统计
 */

public class CountService extends Service {
    private static final String TAG_1 = "CountService";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        send();
        return super.onStartCommand(intent, flags, startId);
    }


    private void send() {
        mCompositeDisposable.add(Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(getObserver()));
    }

    private DisposableObserver getObserver() {
        DisposableObserver disposableObserver = new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                Log.d(TAG_1, "#####开始#####");
                //for count
                Count lastCount = CountDao.getInstance().getLastCount();
                if (lastCount != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(new Date());
                    //时间
                    lastCount.setCreateTime(date);
                    //设备IMEI
                    lastCount.setPadImei(DeviceUtils.getUniqueId(getApplicationContext()));
                    //开机次数
                    lastCount.setOpenPad(DataBase.getInt(AppConfig.openCount));
                    CountDao.getInstance().updateCount(lastCount);
                }

                String countData = new Gson().toJson(CountDao.getInstance().getLastCount() == null ? "" : CountDao.getInstance().getLastCount());
                Log.d(TAG_1, "统计数据: " + countData);
                if (!TextUtils.isEmpty(countData)) {
                    sendDataToServer(countData);
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG_1, "onComplete");
            }

            @Override
            public void onError(Throwable e) {

                Log.e(TAG_1, e.toString(), e);
            }
        };

        return disposableObserver;
    }

    private void sendDataToServer(String countData) {

        //上传统计数据
        try {
            Response response = OkHttpUtils//
                    .postString()//
                    .tag(this)//
                    .mediaType(AppConfig.JSON)
                    .url(Urls.sync)//
                    .content(countData)//
                    .build()//
                    .execute();
            String backData = response.body().string();
            Log.d(TAG_1, "backData: " + backData);
            BaseBean baseBean = GsonSerializator.getInstance().transform(backData, BaseBean.class);
            //发送成功
            if (AppConfig.isRequestOk(baseBean.getCode())) {
                Log.d(TAG_1, "#####发送成功#####");
//                DataBase.saveInt(AppConfig.STARTCOUNT, 0);
//                CountDao.getInstance().delete(1);
//                Count count = new Count();
//                count.setId(1);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                String date = sdf.format(new Date());
//                count.setCreateTime(date);
//                //设备IMEI
//                count.setPadImei(DeviceUtils.getUniqueId(getApplicationContext()));
//                //开机次数
//                count.setOpenPad(DataBase.getInt(AppConfig.STARTCOUNT));
//                CountDao.getInstance().updateCount(count);
            } else { //发送失败
                Log.d(TAG_1, "#####发送失败#####");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
