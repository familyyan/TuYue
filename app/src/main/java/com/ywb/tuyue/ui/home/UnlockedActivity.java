package com.ywb.tuyue.ui.home;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.HomeAdvertBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.LoadImage;
import com.ywb.tuyue.widget.UnLockView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by penghao on 2018/3/14.
 * description：
 */

public class UnlockedActivity extends BaseActivity {
    @BindView(R.id.unLockView)
    UnLockView unLockView;
    @BindView(R.id.bg_advert)
    ImageView bgAdvert;
    //设置自动休眠时间
    private final static int SCREEN_OFF_TIMEOUT = 180 * 1000;

    @Override
    protected int getViewId() {
        return R.layout.activity_unlocked;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setScreenOffTime(SCREEN_OFF_TIMEOUT);
        int screenOffTime = getScreenOffTime();
        Log.d("##screenoff##", "休眠时间" + screenOffTime);
        unLockView.setOnLockListener(new OnLockListener() {
            @Override
            public void onLockListener(boolean isLocked) {
                if (isLocked) {
                    Count lastCount = CountDao.getInstance().getLastCount();
                    if (lastCount != null) {
                        lastCount.setUnLock(lastCount.getUnLock() + 1);
                        CountDao.getInstance().updateCount(lastCount);
                    } else {
                        Count count = new Count();
                        count.setId(1);
                        count.setUnLock(1);
                        CountDao.getInstance().updateCount(count);
                    }

                    UnlockedActivity.Go(mContext, HomePagerActivity.class);
                    finish();
                }
            }
        });
        getAdvertImageUrl();
    }

    private void getAdvertImageUrl() {
        OkHttpUtils//
                .get()//
                .url(MyUrls.getStdbyAdvert)//
                .tag(this)//
                .build()//
                .execute(new AppGsonCallback<HomeAdvertBean>(new RequestModel(mContext).setShowProgress(false)) {
                    @Override
                    public void onResponseOK(HomeAdvertBean response, int id) {
                        super.onResponseOK(response, id);
                        List<HomeAdvertBean.ResultBean> result = response.getResult();
                        if (!result.isEmpty()) {
                            String imageUrl = result.get(0).getImg_url();
                            //通过下载链接 获得存储路径
                            String localPath = MyDownLoadManager.getLocalUrl(imageUrl);
                            LoadImage.loadImageNormal(localPath, bgAdvert);
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
    }

    /**
     * 设置休眠时间 毫秒
     */
    private void setScreenOffTime(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得休眠时间 毫秒
     */
    private int getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {

        }
        return screenOffTime;
    }

    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
