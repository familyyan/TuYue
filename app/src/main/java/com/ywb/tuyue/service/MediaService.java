package com.ywb.tuyue.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.bean.MusicBean;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.downLoad.MyDownLoadManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ywb.tuyue.AppConfig.ACTION_SERVICE_RECEIVER;

/**
 * Created by penghao on 2018/4/27.
 * description： 音乐播放器控制服务
 */

public class MediaService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
    private static final String TAG = "MediaService";
    //初始化MediaPlayer
    public MediaPlayer mMediaPlayer;
    //本地的音乐数据源
    List<MusicBean.ResultBean> mLocalMusicList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private ServiceReceiver serviceReceiver;
    private int currentPostion;//当前进度
    private boolean isRandom;//是否随机
    private boolean isClick;//是否精准点击列表的歌曲
    private int currentIndex;//当前音乐在list中的下标
    private int status;//播放状态
    public static final String STATUS_KEY = "STATUS_KEY";
    public static final String INDEX_KEY = "INDEX_KEY";
    public static final String CURRENT_KEY = "CURRENT_KEY";
    public static final String MAX_KEY = "MAX_KEY";
    public static final String MODE_KEY = "MODE_KEY";
    public static final String CLICK_KEY = "CLICK_KEY";


    public static final int ACTION_MUSIC_PLAY = 1;
    public static final int ACTION_MUSIC_PAUSE = 2;
    public static final int ACTION_MUSIC_NEXT = 3;
    public static final int ACTION_MUSIC_LAST = 4;
    public static final int ACTION_MUSIC_LOAD = 5;
    public static final int ACTION_MUSIC_LOADED = 6;
    public static final int ACTION_MUSIC_PROGRESS = 7;
    public static final int ACTION_MUSIC_SEEK = 8;
    public static final int ACTION_MUSIC_RANDOM = 9;
    private int lastIndex = -1;
    boolean isSendProgress = true;//是否发送进度
    @Override
    public void onCreate() {
        super.onCreate();
        serviceReceiver = new ServiceReceiver();
        registerReceiver(serviceReceiver, new IntentFilter(ACTION_SERVICE_RECEIVER));
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocalData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        isSendProgress = false;
        mHandler.removeCallbacksAndMessages(null);
        if (currentIndex == mLocalMusicList.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex = currentIndex + 1;
        }
        resetMusic();
        playMusic();
        status = ACTION_MUSIC_PROGRESS;
        isSendProgress = true;
        mHandler.post(progressRunable);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("item点击", "加载完毕");
        Intent sendIntent = new Intent(AppConfig.ACTION_ACTIVITY_RECEIVER);
        sendIntent.putExtra(STATUS_KEY, ACTION_MUSIC_LOADED);
        sendIntent.putExtra(INDEX_KEY, currentIndex);
        sendIntent.putExtra(CURRENT_KEY, getPlayPosition());
        sendIntent.putExtra(MAX_KEY, getProgress());
        sendIntent.putExtra(MODE_KEY, isRandom);
        //发送广播，将被Activity中的BroadcastReceiver接收到
        sendBroadcast(sendIntent);
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("info", i + "________________");
        return false;
    }

    class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            status = intent.getExtras().getInt(STATUS_KEY);
            currentIndex = intent.getExtras().getInt(INDEX_KEY);
            isRandom = intent.getExtras().getBoolean(MODE_KEY);
            isClick = intent.getExtras().getBoolean(CLICK_KEY);
            lastIndex = currentIndex;
            switch (status) {
                case ACTION_MUSIC_LOAD:
                    Log.e("item点击", "准备加载");
                    resetMusic();
                    Log.d(TAG, "status: ACTION_MUSIC_LOAD");
                    break;
                case ACTION_MUSIC_PLAY://播放
                    Log.d(TAG, "status: ACTION_MUSIC_PLAY");
                    playMusic();
                    status = ACTION_MUSIC_PROGRESS;
                    isSendProgress = true;
                    mHandler.post(progressRunable);
                    break;
                case ACTION_MUSIC_PAUSE://暂停
                    Log.d(TAG, "status: ACTION_MUSIC_PAUSE");
                    status = ACTION_MUSIC_PAUSE;
                    pauseMusic();
                    isSendProgress = true;
                    break;
                case ACTION_MUSIC_SEEK://拖动
                    status = ACTION_MUSIC_PROGRESS;
                    currentPostion = intent.getExtras().getInt(CURRENT_KEY);
                    seekToPositon(currentPostion);
                    break;
            }
        }
    }

    /**
     * 更新ui的runnable
     */
    private Runnable progressRunable = new Runnable() {
        @Override
        public void run() {
            //广播通知Activity更改图标、文本框
            Intent sendIntent = new Intent(AppConfig.ACTION_ACTIVITY_RECEIVER);
            sendIntent.putExtra(STATUS_KEY, status);
            sendIntent.putExtra(INDEX_KEY, currentIndex);
            sendIntent.putExtra(CURRENT_KEY, getPlayPosition());
            sendIntent.putExtra(MAX_KEY, getProgress());
            sendIntent.putExtra(MODE_KEY, isRandom);
            //发送广播，将被Activity中的BroadcastReceiver接收到
            sendBroadcast(sendIntent);
            if (isSendProgress) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void initLocalData() {
        String localData = DataBase.getString(AppConfig.MUSIC_DATA);
        if (!TextUtils.isEmpty(localData)) {
            MusicBean musicBean = new Gson().fromJson(localData, MusicBean.class);
            mLocalMusicList.clear();
            if (musicBean != null) {
                List<MusicBean.ResultBean> result = musicBean.getResult();
                if (result != null && !result.isEmpty()) {
                    mLocalMusicList.addAll(result);
                }
            }
        }
    }

    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    private void initMediaPlayerFile(int currentIndex) {
        //获取文件路径
        try {
            if (mLocalMusicList != null && !mLocalMusicList.isEmpty()) {
                String music = mLocalMusicList.get(currentIndex).getMusic();
                String localUrl = MyDownLoadManager.getLocalUrl(music);
                //此处的两个方法需要捕获IO异常
                //设置音频文件到MediaPlayer对象中
                mMediaPlayer.setDataSource(localUrl);
                //让MediaPlayer对象准备
                mMediaPlayer.prepare();
            }
        } catch (IOException e) {
            Log.d(TAG, "设置资源，准备阶段出错");
            e.printStackTrace();
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic() {
        if (!mMediaPlayer.isPlaying()) {
            //如果还没开始播放，就开始
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            //如果还没开始播放，就开始
            mMediaPlayer.pause();
        }
    }

    /**
     * reset
     */
    public void resetMusic() {
        //如果还没开始播放，就开始
        mMediaPlayer.reset();
        if (isRandom && !isClick) {
            currentIndex = createRandom();
        }
        initMediaPlayerFile(currentIndex);
    }

    int lastRandomInt;

    /**
     * 生产不重复的随机数
     * @return
     */
    private int createRandom() {
        Random random = new Random();
        int randomInt = random.nextInt(mLocalMusicList.size());
        if (lastRandomInt == randomInt || currentIndex == randomInt) {
            if (randomInt == mLocalMusicList.size() - 1) {
                randomInt = 0;
            } else {
                randomInt = randomInt + 1;
            }
        }
        return randomInt;
    }

    /**
     * 关闭播放器
     */

    public void closeMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    /**
     * 获取歌曲长度
     **/
    public int getProgress() {
        return mMediaPlayer.getDuration();
    }

    /**
     * 获取播放位置
     */
    public int getPlayPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 播放指定位置
     */
    public void seekToPositon(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    @Override
    public void onDestroy() {
        //我们的handler发送是定时1000s发送的，如果不关闭，MediaPlayer release掉了还在获取getCurrentPosition就会爆IllegalStateException错误
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
