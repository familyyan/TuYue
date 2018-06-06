package com.ywb.tuyue.ui.movie;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.MovieSyncDataBean;
import com.ywb.tuyue.db.dao.BannerDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.utils.LoadImage;

import java.util.concurrent.TimeUnit;

import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyMovieJZVideoPlayerStandard extends JZVideoPlayerStandard {

    private static final String TAG_1 = "MyMovieJZVideo";

    private ImageView advert;
    private TextView second;

    private int secondStr;

    public MyMovieJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyMovieJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.my_jz_layout;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        advert = findViewById(R.id.my_custom_advert);
        second = findViewById(R.id.my_custom_second);
        MovieSyncDataBean.ResultBean.MovieBannerBean banner = BannerDao.getInstance().getLastBanner();
        if (banner != null) {
            secondStr = banner.getDuration();
            LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(banner.getImg_url()), advert);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
//                setViewGone(true);
            } else {
                //click goto fullscreen
//                setViewGone(false);
            }
        } else if (i == R.id.surface_container) {
            if (currentState == CURRENT_STATE_NORMAL){
                return;
            }

        } else if (i == R.id.start) {
            if (currentState == CURRENT_STATE_NORMAL) {
                advert.setVisibility(VISIBLE);
                second.setVisibility(VISIBLE);
                Observable//
                        .interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                        .take(secondStr + 1).map(aLong -> secondStr - aLong)//设置循环secondStr+1次
                        .observeOn(AndroidSchedulers.mainThread())//
                        .subscribeOn(Schedulers.io())//
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                second.setText("广告剩余 " + aLong + " 秒");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                advert.setVisibility(GONE);
                                second.setVisibility(GONE);
                                startVideo();
                            }
                        });

            }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
        Log.d(TAG_1, "startVideo:用户触发的视频开始播放 ");
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        Log.d(TAG_1, "onStateNormal: 控件进入普通的未播放状态");
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        Log.d(TAG_1, "onStatePreparing: 进入preparing状态，正在初始化视频");
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        Log.d(TAG_1, "onStatePlaying: preparing之后进入播放状态");
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        Log.d(TAG_1, "onStatePause: 暂停视频，进入暂停状态");
    }

    @Override
    public void onStateError() {
        super.onStateError();
        Log.d(TAG_1, "onStateError: 进入错误状态");
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        Log.d(TAG_1, "onStateAutoComplete:进入视频自动播放完成状态 ");
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
        Log.d(TAG_1, "onInfo: ");
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        Log.d(TAG_1, "onError: ");
    }


    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
        Log.d(TAG_1, "startWindowTiny:退出全屏 ");
    }


}
