package com.ywb.tuyue.ui.web;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.ywb.tuyue.R;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.UiHelper;
import com.ywb.tuyue.utils.UnZip;
import com.ywb.tuyue.widget.HeaderView;

import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by penghao on 2018/3/20.
 * description：
 */

public class ThirdPartyActivity extends BaseActivity {
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private AgentWeb agentWeb;
    private String titleStr, url;

    @Override
    protected int getViewId() {
        return R.layout.activity_third;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initView(Bundle savedInstanceState) {
        titleStr = getIntent().getExtras().getString("title");
        String contentStr = getIntent().getExtras().getString("content");
        Log.d("initView-->", "contentStr: " + contentStr);
        UiHelper.getInstance().showLoading(mContext);
        if (!TextUtils.isEmpty(contentStr) && contentStr.endsWith("zip")) {
            unGameZip(contentStr);
        } else {
            url = "file://" + contentStr;
            goWeb();
        }
    }

    private static final String webPath = "/storage/emulated/0/Android/data/com.ywb.tuyue/files/Download/web/";
    File indexFile;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void unGameZip(String thirdUrl) {
        Observable.create(subscribe -> {
            String downLoadPath = MyDownLoadManager.getLocalUrl(thirdUrl);
            if (!TextUtils.equals(thirdUrl, downLoadPath)) {
                Log.d("unZip--->", "downLoadPath: " + downLoadPath);
                File outFile = new File(downLoadPath);
                String outPath = outFile.getParent() + "/web/";
                Log.d("unZip--->", "outPath: " + outPath);
                try {
                    File file = new File(outPath + titleStr);
                    if (!file.exists()) {
                        UnZip.UnZipFolder(downLoadPath, outPath + titleStr);
                        Log.d("unZip--->", "UnZipFolder: " + outPath + titleStr);
                        //解压完后，如果zip文件存在，就删除。
                        if (outFile.exists()) {
                            outFile.delete();
                        }
                    } else {
                        Log.d("unZip--->", "存在吗: " + outFile.exists());
                        //如果新的zip文件存在，就重新解压。
                        if (outFile.exists()) {
                            //删除文件夹和其文件
//                    deleteFile(file);
                            UnZip.UnZipFolder(downLoadPath, outPath + titleStr);
                            Log.d("unZip--->", "新的zip文件: " + outPath + titleStr);
                            outFile.delete();
                        }
                    }
                    indexFile = new File(outPath + titleStr + "/index.html");

                } catch (Exception e) {
                    e.printStackTrace();
                    indexFile = new File(webPath + titleStr + "/index.html");
                }
            } else {
                indexFile = new File(webPath + titleStr + "/index.html");
            }
            subscribe.onNext(Activity.RESULT_OK);
            subscribe.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Integer -> {
            UiHelper.getInstance().dismissLoading();
            if (indexFile.exists()) {
                url = "file://" + indexFile.getAbsolutePath();
            } else {
                Toast.makeText(mContext, "缺少index.html文件", Toast.LENGTH_SHORT).show();
            }
            goWeb();
        });

    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(titleStr);
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!agentWeb.back()) {
                    finish();
                } else {
                    agentWeb.back();
                }
            }
        });
    }

    private void goWeb() {
        try {
            agentWeb = AgentWeb//
                    .with(mContext)//
                    .setAgentWebParent(rootView, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()//
                    .defaultProgressBarColor()//
                    .createAgentWeb()//
                    .ready()//
                    .go(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb != null) {
            if (agentWeb.handleKeyEvent(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (agentWeb != null)
            agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (agentWeb != null)
            agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (agentWeb != null)
            agentWeb.getWebLifeCycle().onDestroy();
        title.unRegister();
        super.onDestroy();
    }
}
