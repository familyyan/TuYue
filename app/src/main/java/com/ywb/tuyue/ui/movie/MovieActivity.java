package com.ywb.tuyue.ui.movie;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Movie;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.widget.HeaderView;

import butterknife.BindView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by mhdt on 2017/12/16.
 * 电影
 */

public class MovieActivity extends BaseActivity {
    private static final String TAG = "mMovieActivity";
    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.movie_name)
    TextView movieName;
    @BindView(R.id.movie_director)
    TextView movieDirector;
    @BindView(R.id.movie_actor)
    TextView movieActor;
    @BindView(R.id.movie_profile)
    TextView movieProfile;

    //节操播放器
    MyMovieJZVideoPlayerStandard player;
    public static final String FILE_PATH = AppConfig.DEFAULT_SAVE_PATH + "movie/";

    //视频地址(测试地址)
    private String url = "http://m9.play.vp.autohome.com.cn/flvs/B7823166A8F03C31/2017-02-06/A25A5EE927056AFE-100.mp4?key=2007AADDC7E922DCF213393C206602EC&time=1486458174";
    private String thumbUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";

    Movie.ResultBean movieBean;
    private boolean isLocal;

    private String localPath = AppConfig.SDCARD_ROOT_PATH + "/01b1ef8a923049f134fc9a5b640f9fc2.mp4";


    @Override
    protected int getViewId() {
        return R.layout.activity_movie;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        resolverIntent();
        initView();
    }

    private void initView() {
        player = findViewById(R.id.videoplayer);
//        url = "/storage/emulated/0/com.ywb.tuyue/movie/test1.mp4";
        Log.d(TAG, "initView: " + url);
        Log.d(TAG, "DataSource: " + JZMediaManager.getCurrentDataSource());

        player.setUp(url, JZVideoPlayer.SCREEN_WINDOW_NORMAL, movieBean.getName());

        try {
            //设置视频第一帧缩略图
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(url);
            Bitmap bitmap = media.getFrameAtTime();
            player.thumbImageView.setImageBitmap(bitmap);
        } catch (Exception e) {

        }

    }


    /**
     * 解析传入的实体类
     */
    private void resolverIntent() {
        movieBean = (Movie.ResultBean) getIntent().getExtras().getSerializable("movieBean");
        isLocal = getIntent().getExtras().getBoolean("isLocal");
        try {
            movieName.setText(movieBean.getName());
            movieDirector.setText("导演：" + movieBean.getDirector());
            movieActor.setText("主演：" + movieBean.getActor());
            movieProfile.setText("内容简介：" + movieBean.getDetail());
            url = isLocal ? movieBean.getDownload_url() : movieBean.getSource_url();
//            url = AppConfig.DEFAULT_SAVE_PATH + "ceshi1905.ts";
        } catch (NullPointerException e) {

        }
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        header.setTitleOnly(R.string.movie_title);
        header.setRightBtnVisiable(View.GONE);
        header.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    public void onBackPressed() {
        if (player.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.releaseAllVideos();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Log.d(TAG, "竖屏");
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "横屏");
        }

    }

    @Override
    protected void onDestroy() {
//        player.releaseAllVideos();
//        JZVideoPlayer.releaseAllVideos();
//        JZVideoPlayer.clearSavedProgress(mContext, url);
//        player.resetProgressAndTime();
//        player = null;
        header.unRegister();
        super.onDestroy();
    }
}
