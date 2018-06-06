package com.ywb.tuyue.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.ui.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by penghao on 2018/1/25.
 * description：
 */

public class HomePagerActivity extends BaseActivity {

    TextView second;
    Timer timer = new Timer();
    int currentSecond = 4;
    int minTime = 0;

    @Override
    protected int getViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        second = findViewById(R.id.second);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSecond--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        second.setText(currentSecond + "秒跳过");
                    }
                });
                if (currentSecond == minTime) {
                    start2Main();
                }

            }
        }, 0, 1000);
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start2Main();
            }
        });
    }


    private void start2Main() {
        if (timer != null) {
            timer.cancel();
        }

        MainActivity.Go(mContext, MainActivity.class);
        finish();
    }
}
