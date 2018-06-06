package com.ywb.tuyue.ui.setting;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.BuildConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.bean.VersionBean;
import com.ywb.tuyue.db.SharedPreferenceHelper;
import com.ywb.tuyue.downLoad.DownLoadEntity;
import com.ywb.tuyue.downLoad.DownLoadTask;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.FileUtils;
import com.ywb.tuyue.utils.StringUtils;
import com.ywb.tuyue.utils.SystemUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by penghao on 2017/12/18.
 * description：系统版本
 */

public class SystemVersionFrag extends BaseFragment {
    private static final String TAG = "SystemVersionFrag";
    @BindView(R.id.data_versionStatus)
    TextView dataVersionStatus;
    @BindView(R.id.data_click_upGrade)
    TextView dataClickUpGrade;
    @BindView(R.id.data_CurrentVersion)
    TextView dataCurrentVersion;
    @BindView(R.id.ll_sys_time)
    LinearLayout llSysTime;
    @BindView(R.id.ll_update)
    LinearLayout update;
    @BindView(R.id.is_new)
    TextView isNew;
    @BindView(R.id.currentVersion)
    TextView currentVersion;
    @BindView(R.id.city_sync)
    TextView citySync;

    private VersionBean.ResultBean versionBean;

    ProgressBar progressBar;
    TextView loadingPercent;
    TextView loadingState, downLoadTitle;

    /**
     * 下载进度
     */
    private void showUpGradeDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_upgrade, null);
        progressBar = view.findViewById(R.id.progressbar);
        loadingPercent = view.findViewById(R.id.loadingPercent);
        loadingState = view.findViewById(R.id.loadingState);
        downLoadTitle = view.findViewById(R.id.down_load_title);
        downLoadTitle.setText("下载新版本，请稍候");
        final AlertDialog dialog = new AlertDialog.Builder(mActivity).setView(view).create();
        dialog.show();
        loadingState.setOnClickListener(v -> {
            for (Map.Entry<Long, Disposable> entry : requestTaskMap.entrySet()) {
                //取消查询任务
                Disposable dd = entry.getValue();
                if (dd != null) {
                    dd.dispose();
                }
            }
            requestTaskMap.clear();
            dialog.dismiss();
        });
    }

    @Override
    protected int getViewId() {
        return R.layout.frag_data_syn;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        citySync.setVisibility(View.GONE);
        initUiVisible();
    }

    private void initUiVisible() {
        llSysTime.setVisibility(View.GONE);
        try {
            //当前版本
            currentVersion.setText("当前系统版本：");
            dataCurrentVersion.setText("v" + SystemUtils.getVersionName(mActivity));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkVersionUpgrade();
    }

    @OnClick(R.id.data_click_upGrade)
    public void onViewClicked() {
        //更新逻辑
        showAppUpGardDialog();
    }

    /**
     * 检查版本更新
     */
    private void checkVersionUpgrade() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(Urls.selectVersionInfo)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("type", "1")//type:1用户端，2服务端
                .build()//
                .execute(new AppGsonCallback<VersionBean>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(VersionBean response, int id) {
                        super.onResponseOK(response, id);
                        versionBean = response.getResult();
                        try {
                            if (versionBean != null) {
                                double versionCode = Double.parseDouble(versionBean.getVersionNumber());
                                int currentCode = SystemUtils.getVersionCode(mActivity);
                                Log.d("codeInfo", "versionCode," + versionCode + ",currentCode,"
                                        + currentCode);
                                if (currentCode < versionCode) {//如果当前版本低于服务器给的版本
                                    dataVersionStatus.setText(versionBean.getVersion() + "");
                                    update.setVisibility(View.VISIBLE);
                                    isNew.setVisibility(View.GONE);
                                } else {
                                    if (update != null) {

                                        update.setVisibility(View.GONE);
                                    }
                                    if (isNew != null) {

                                        isNew.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                });
//        }
    }

    /**
     * 版本升级对话框
     */
    private void showAppUpGardDialog() {
        if (versionBean != null) {
            new AlertDialog.Builder(mActivity)//
                    .setMessage("检测到最新版本" + versionBean.getVersion() + "，大小为" + versionBean
                            .getApkSize() + "，更新内容："+versionBean.getContent()+"，是否更新？")//
                    .setNegativeButton("以后再说", null)//
                    .setPositiveButton("更新", (dialog, which) -> {
                        downLoadTaskQueue.offer(new DownLoadTask(StringUtils.dealStrAPP(versionBean.getDownloadUrl()), FileUtils.getFileName(versionBean.getDownloadUrl()), DownLoadTask.UNKNOW, -1));
                        executeTask();
                    }).show();//
        }
    }

    private Queue<DownLoadTask> downLoadTaskQueue = new LinkedList<>();
    boolean isHaseRealTask = false;
    private Map<Long, Disposable> requestTaskMap = new HashMap<>();

    /**
     * 从任务队列中取出数据执行下载操作
     */
    private void executeTask() {
        DownLoadTask downLoadTask = downLoadTaskQueue.poll();
        if (downLoadTask != null) {
            try {
                startDown(downLoadTask);
            } catch (Exception e) {
                Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {//下载完成
            Toast.makeText(mActivity, "恭喜您,已是最新数据了!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下载文件
     *
     * @param downLoadTask
     */
    private void startDown(DownLoadTask downLoadTask) {
        if (TextUtils.isEmpty(downLoadTask.getDownLoadUrl())) {
            executeTask();
            Log.d(TAG, "下载地址为空 执行下一个任务！************************>");
            return;
        }
        long taskId = MyDownLoadManager.getDownLoadId(downLoadTask.getDownLoadUrl());
        DownLoadEntity DEntity = MyDownLoadManager.getDownLoadEntity(taskId);

        //过滤已完成的任务
        if (DEntity != null && DEntity.getCOLUMN_STATUS() == DownloadManager.STATUS_SUCCESSFUL) {
            executeTask();
            Log.d(TAG, "上条任务存在并且已经完成！************************>");
            return;
        }

        if (DEntity == null) {//任务不存在（断点续传）
            Log.d(TAG, "上条任务不存在！************************>");
            isHaseRealTask = true;
            DownloadManager.Request request = MyDownLoadManager.buildRequest(downLoadTask);
            taskId = MyDownLoadManager.getDownloadManager().enqueue(request);
            SharedPreferenceHelper.saveLong(downLoadTask.getDownLoadUrl(), taskId);
            //下载对话框
            showUpGradeDialog();

        } else {
            Log.d(TAG, "上条任务已经存在！---------------------->");
        }

        long finalTaskId = taskId;

        Log.d(TAG, finalTaskId + "__________" + downLoadTask.toString());

        Disposable disposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .flatMap(count -> {
                    DownLoadEntity downLoadEntity = MyDownLoadManager.getDownLoadEntity(finalTaskId);
                    return Observable.just(downLoadEntity);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    float pro = message.getFormatDownLoadPregress();

                    Log.d("SSSSSSSSSS", "任务查询结果----状态--->" + MyDownLoadManager.getStatuDesc(message.getCOLUMN_STATUS()));
                    Log.d("SSSSSSSSSS", "任务查询结果----存储地址--->" + message.getCOLUMN_LOCAL_URI());

                    if (progressBar != null) {
                        progressBar.setProgress((int) pro);
                    }

                    if (loadingPercent != null) {
                        loadingPercent.setText(pro + "%");//设置进度条进度
                    }

                    switch (message.getCOLUMN_STATUS()) {
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            Log.d(TAG, "任务查询结果----状态--->" + MyDownLoadManager.getStatuDesc(message.getCOLUMN_STATUS()));
                            Log.d(TAG, "任务查询结果----存储地址--->" + message.getCOLUMN_LOCAL_URI());

                            //取消任务
                            Disposable d = requestTaskMap.get(finalTaskId);
                            if (d != null) {
                                d.dispose();
                            }
                            requestTaskMap.remove(finalTaskId);
                            String localUrl = MyDownLoadManager.getLocalUrl(downLoadTask.getDownLoadUrl());
                            Log.d(TAG, "任务查询结果----存储地址111--->" + localUrl);
                            installApk(new File(localUrl));
                            executeTask();
                        }

                        break;
                        case DownloadManager.STATUS_RUNNING: {
                            if (loadingState != null) {
                                loadingState.setText("正在下载");
                            }
                            String availableSize = SystemUtils.getSDAvailableSize(mActivity);

                            if (!TextUtils.isEmpty(availableSize)) {//当SD卡内存不足5M时
                                String sub = availableSize.replace(" GB", "");
                                double size1 = Double.parseDouble(sub);
                                if (size1 < 0.00488) {
                                    if (downLoadTask != null) {
                                        //取消任务
                                        Disposable dd = requestTaskMap.get(finalTaskId);
                                        if (dd != null) {
                                            dd.dispose();
                                        }
                                        requestTaskMap.remove(finalTaskId);
                                    }
                                    Toast.makeText(mActivity, "剩余空间不足", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case DownloadManager.STATUS_PENDING:
                            if (loadingState != null) {
                                loadingState.setText("等待下载");
                            }
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            if (loadingState != null) {
                                loadingState.setText("下载暂停");
                            }
                            break;
                        case DownloadManager.STATUS_FAILED:
                            if (loadingState != null) {
                                loadingState.setText("下载失败");
                            }
                            break;
                    }
                });

        requestTaskMap.put(taskId, disposable);
    }

    @Override
    public void onDestroyView() {
        for (Map.Entry<Long, Disposable> entry : requestTaskMap.entrySet()) {
            //取消查询任务
            Disposable dd = entry.getValue();
            if (dd != null) {
                dd.dispose();
            }
        }
        requestTaskMap.clear();
        super.onDestroyView();

    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mActivity,
                    BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
