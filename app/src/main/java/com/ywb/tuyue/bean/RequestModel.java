package com.ywb.tuyue.bean;

import android.content.Context;

/**
 * Created by mhdt on 2016/11/22.
 * update
 */

public class RequestModel {
    private Context c;
    private boolean isShowProgress = true;//是否显示进度条
    private boolean isShowError = true;//是否打印错误信息
    private boolean isAutoDealOtherCase = true;//是否处理别的情形
    private boolean isOnlyDealSuccess = true;//是否只解析成功的情况

    public RequestModel(Context c) {
        this.c = c;
    }

    public Context getC() {
        return c;
    }

    public RequestModel setC(Context c) {
        this.c = c;
        return this;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public RequestModel setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
        return this;
    }

    public boolean isShowError() {
        return isShowError;
    }

    public RequestModel setShowError(boolean showError) {
        isShowError = showError;
        return this;
    }

    public boolean isAutoDealOtherCase() {
        return isAutoDealOtherCase;
    }

    public RequestModel setAutoDealOtherCase(boolean autoDealOtherCase) {
        isAutoDealOtherCase = autoDealOtherCase;
        return this;
    }

    public boolean isOnlyDealSuccess() {
        return isOnlyDealSuccess;
    }

    public RequestModel setOnlyDealSuccess(boolean onlyDealSuccess) {
        isOnlyDealSuccess = onlyDealSuccess;
        return this;
    }
}
