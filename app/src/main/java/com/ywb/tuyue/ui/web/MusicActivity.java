package com.ywb.tuyue.ui.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.MusicAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.MusicBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.service.MediaService;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.HTMLFormat;
import com.ywb.tuyue.utils.LoadImage;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.widget.HeaderView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by penghao on 2018/4/20.
 * description： 音乐
 */

public class MusicActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.music_list)
    RecyclerView musicList;
    @BindView(R.id.music_img)
    ImageView musicImg;
    @BindView(R.id.lyrics)
    WebView lyrics;
    @BindView(R.id.music_play)
    ImageView musicPlay;
    @BindView(R.id.music_random)
    ImageView musicRandom;
    @BindView(R.id.music_currentTime)
    TextView musicCurrentTime;
    @BindView(R.id.music_progress)
    SeekBar musicProgress;
    @BindView(R.id.music_totalTime)
    TextView musicTotalTime;
    @BindView(R.id.music_vol)
    SeekBar musicVol;

    private List<MusicBean.ResultBean> resultBeans = new ArrayList<>();

    private long lastMill;
    MusicAdapter musicAdapter;
    private MyReceiver myReceiver;
    private VolumeReceiver volumeReceiver;
    private int status = MediaService.ACTION_MUSIC_LOAD, index, lastIndex = -1, currentProgress, maxDuration;
    private boolean isRandom;

    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    AudioManager am;

    @OnClick({R.id.music_play, R.id.music_random, R.id.music_last, R.id.music_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.music_play:
                boolean isPlay = (boolean) view.getTag();
                view.setTag(isPlay = !isPlay);
                if (status == MediaService.ACTION_MUSIC_LOAD) {
                    sendIntent(MediaService.ACTION_MUSIC_LOAD, index, isRandom);
                    return;
                }
                if (isPlay) {
                    sendIntent(MediaService.ACTION_MUSIC_PLAY, index, isRandom);
                    musicPlay.setImageResource(R.drawable.music_paus);
                } else {
                    sendIntent(MediaService.ACTION_MUSIC_PAUSE, index, isRandom);
                    musicPlay.setImageResource(R.drawable.music_play);
                }
                break;
            case R.id.music_random://随机
//                boolean isRandom = (boolean) view.getTag();
//                view.setTag(isRandom = !isRandom);
                isRandom = !isRandom;
                if (isRandom) {
                    musicRandom.setImageResource(R.drawable.music_suiji);
                } else {
                    musicRandom.setImageResource(R.drawable.music_order);
                }
                sendIntent(status, index, isRandom);
                break;
            case R.id.music_last://上一曲
                if (!isRandom) {
                    index = index == 0 ? resultBeans.size() - 1 : index - 1;
                }
                status = MediaService.ACTION_MUSIC_LOAD;
                sendIntent(status, index, isRandom);
                break;
            case R.id.music_next://下一曲
                if (!isRandom) {
                    index = index == resultBeans.size() - 1 ? 0 : index + 1;
                }
                status = MediaService.ACTION_MUSIC_LOAD;
                sendIntent(status, index, isRandom);
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
        if (fromUser) {
            sendIntent(MediaService.ACTION_MUSIC_SEEK, index, seekBar.getProgress(), isRandom);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                musicVol.setProgress(currentVolume);
            }
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                status = intent.getExtras().getInt(MediaService.STATUS_KEY);
                index = intent.getExtras().getInt(MediaService.INDEX_KEY);
                currentProgress = intent.getExtras().getInt(MediaService.CURRENT_KEY);
                maxDuration = intent.getExtras().getInt(MediaService.MAX_KEY);
                isRandom = intent.getExtras().getBoolean(MediaService.MODE_KEY);
                Log.d("MyReceiver", "onReceive: " + status + "," + index + "," + lastIndex + "," + currentProgress + "," + maxDuration);
                switch (status) {
                    case MediaService.ACTION_MUSIC_PROGRESS://正在播放
                        musicPlay.setImageResource(R.drawable.music_paus);
                        break;
                    case MediaService.ACTION_MUSIC_PAUSE://正在播放
                        musicPlay.setImageResource(R.drawable.music_play);
                        break;
                    case MediaService.ACTION_MUSIC_LOADED://音乐加载完毕
                        Log.e("item点击", "收到加载完毕");
                        musicPlay.setTag(false);
                        musicPlay.performClick();
                        break;
                }
                musicCurrentTime.setText(time.format(currentProgress));
                musicProgress.setMax(maxDuration);
                musicProgress.setProgress(currentProgress);
                musicRandom.setImageResource(isRandom ? R.drawable.music_suiji : R.drawable.music_order);
                if (lastIndex != index) {
                    String string = DataBase.getString(AppConfig.MUSIC_DATA);
                    if (!TextUtils.isEmpty(string)) {
                        MusicBean musicBean = new Gson().fromJson(string, MusicBean.class);
                        if (musicBean != null) {
                            List<MusicBean.ResultBean> result = musicBean.getResult();
                            if (result != null && !result.isEmpty()) {
                                MusicBean.ResultBean resultBean = result.get(index);
                                if (resultBean != null) {
                                    String img = resultBean.getImg();
                                    LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(img), musicImg);
                                    lyrics.loadDataWithBaseURL(null, HTMLFormat.getNewContent(resultBean.getLyrics()), "text/html", "utf-8", null);
                                    musicTotalTime.setText(resultBean.getDuration());
                                }
                                musicAdapter.updateIndex(index);
                            }
                        }

                    }
                    lastIndex = index;
                }
            }
        }
    }
    @Override
    protected int getViewId() {
        return R.layout.activity_music;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        musicPlay.setTag(false);
        lastMill = System.currentTimeMillis();
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, new IntentFilter(AppConfig.ACTION_ACTIVITY_RECEIVER));
        musicProgress.setOnSeekBarChangeListener(this);
        Intent intent = new Intent(mContext, MediaService.class);
        //启动后台Service
        startService(intent);
        setVolume();
        musicList.setHasFixedSize(true);
        musicList.setLayoutManager(new LinearLayoutManager(mContext));
        musicList.setAdapter(musicAdapter = new MusicAdapter(mContext, resultBeans));
        musicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                if (pos == musicAdapter.getCurrentIndex()) {
                    return;
                }
                resetMusicInfo(pos);
            }
        });
        getData();
    }

    /**
     * 重置歌曲信息
     *
     * @param pos
     */
    private void resetMusicInfo(int pos) {
        MusicBean.ResultBean resultBean = resultBeans.get(pos);
        if (resultBean != null) {
            String img = resultBean.getImg();
            LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(img), musicImg);
            lyrics.loadDataWithBaseURL(null, HTMLFormat.getNewContent(resultBean.getLyrics()), "text/html", "utf-8", null);
            musicTotalTime.setText(resultBean.getDuration());
        }
        musicAdapter.updateIndex(pos);
        index = pos;
        status = MediaService.ACTION_MUSIC_LOAD;
        sendIntent(status, index, isRandom, true);
    }

    private void setVolume() {
        volumeReceiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeReceiver, filter);
        //volume
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        musicVol.setMax(maxVolume);
        //获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        musicVol.setProgress(currentVolume);
        musicVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 发送广播
     *
     * @param status 播放状态
     * @param index  播放下标
     */
    private void sendIntent(int status, int index, boolean isRandom) {
        Intent intent = new Intent(AppConfig.ACTION_SERVICE_RECEIVER);
        intent.putExtra(MediaService.STATUS_KEY, status);
        intent.putExtra(MediaService.INDEX_KEY, index);
        intent.putExtra(MediaService.MODE_KEY, isRandom);
        intent.putExtra(MediaService.CLICK_KEY, false);
        sendBroadcast(intent);
    }

    private void sendIntent(int status, int index, boolean isRandom, boolean isClick) {
        Intent intent = new Intent(AppConfig.ACTION_SERVICE_RECEIVER);
        intent.putExtra(MediaService.STATUS_KEY, status);
        intent.putExtra(MediaService.INDEX_KEY, index);
        intent.putExtra(MediaService.MODE_KEY, isRandom);
        intent.putExtra(MediaService.CLICK_KEY, isClick);
        sendBroadcast(intent);
    }

    private void sendIntent(int status, int index, int currentProgress, boolean isRandom) {
        Intent intent = new Intent(AppConfig.ACTION_SERVICE_RECEIVER);
        intent.putExtra(MediaService.STATUS_KEY, status);
        intent.putExtra(MediaService.INDEX_KEY, index);
        intent.putExtra(MediaService.CURRENT_KEY, currentProgress);
        intent.putExtra(MediaService.MODE_KEY, isRandom);
        intent.putExtra(MediaService.CLICK_KEY, false);
        sendBroadcast(intent);
    }

    private void getData() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.getMusic)//
                .build()//
                .execute(new AppGsonCallback<MusicBean>(new RequestModel(mContext)) {
                    @Override
                    public void onResponseOK(MusicBean response, int id) {
                        super.onResponseOK(response, id);
                        List<MusicBean.ResultBean> result = response.getResult();
                        resultBeans.clear();
                        if (result != null && !result.isEmpty()) {
                            resultBeans.addAll(result);
                        }
                        musicAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //屏蔽物理按键 不弹出系统自带的调节音量控件
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
        }
        return true;
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(U.getString(R.string.music_title));
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.d("ddddddd", "onDestroy: ");
        title.unRegister();
        OkHttpUtils.getInstance().cancelTag(this);
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setMusicTime(lastCount.getMusicTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        unregisterReceiver(myReceiver);
        unregisterReceiver(volumeReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ddddddd", "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ddddddd", "onStop: ");
        sendIntent(MediaService.ACTION_MUSIC_PAUSE, index, isRandom);
    }
}
