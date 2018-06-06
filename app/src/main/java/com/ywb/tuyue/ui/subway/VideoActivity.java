package com.ywb.tuyue.ui.subway;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.ui.BaseActivity;


/**
 * Created by penghao on 2018/1/11.
 * description：
 */

public class VideoActivity extends BaseActivity {
    private static final String TAG = "mVideoActivity";
    //节操播放器
    MyJZVideoPlayerStandard player;

    //视频地址(测试地址)
    private String url = "http://m9.play.vp.autohome.com.cn/flvs/B7823166A8F03C31/2017-02-06/A25A5EE927056AFE-100.mp4?key=2007AADDC7E922DCF213393C206602EC&time=1486458174";
    //    private String m3u8Url = "https://logos-channel.scaleengine.net/logos-channel/live/biblescreen-ad-free/playlist.m3u8";
    private String mThumbUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
    private String thumbUrl;
    private String title;

    @Override
    protected int getViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        player = findViewById(R.id.videoplayer);
        url = getIntent().getExtras().getString("content");
        thumbUrl = getIntent().getExtras().getString("screenshots");
        title = getIntent().getExtras().getString("title");
        Log.d(TAG, "播放地址：" + url);
        //设置进入页面全屏
        player.startFullscreen(this, MyJZVideoPlayerStandard.class, url, title);
//        player.setUp(url, JZVideoPlayer.SCREEN_WINDOW_NORMAL, title);
        player.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.releaseAllVideos();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            player.startFullscreen(this, MyJZVideoPlayerStandard.class, url, title);
            Log.d(TAG, "竖屏");
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "横屏");
        }
    }

//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: ");
//        player.releaseAllVideos();
//        JZVideoPlayer.releaseAllVideos();
//        JZVideoPlayer.clearSavedProgress(mContext, url);
//        player.resetProgressAndTime();
//        player = null;
//        super.onDestroy();
//    }
}
