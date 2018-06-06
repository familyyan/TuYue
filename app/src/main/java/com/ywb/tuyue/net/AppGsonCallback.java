package com.ywb.tuyue.net;

import android.util.Log;
import android.widget.Toast;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.BaseBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.utils.UiHelper;
import com.zhy.http.okhttp.callback.GenericsCallback;

import okhttp3.Call;
import okhttp3.Request;


/**
 * 请求返回预处理
 *
 * @author mhdt
 * @version 1.0
 * @created 2017/2/4
 */
public class AppGsonCallback<T> extends GenericsCallback<T> {
    private RequestModel requestModle;

    public AppGsonCallback(RequestModel requestModle) {
        super(GsonSerializator.getInstance());
        this.requestModle = requestModle;
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        Log.d("AppGsonCallback", "onBefore....");
        if (requestModle.isShowProgress()) {
            //显示加载网络对话框
//            dialog = UIHelper.showLoginDialog(requestModle.getC());
            UiHelper.getInstance().showLoading(requestModle.getC());
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("AppGsonCallback", "onError...." + e.getMessage());
        if (requestModle.isShowError())
            Toast.makeText(requestModle.getC(), U.getString(R.string.requestfailed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(T response, int id) {
        Log.d("AppGsonCallback", "onResponse....");
        BaseBean baseBean = (BaseBean) response;
        if (requestModle.isOnlyDealSuccess()) {
            if (response != null) {
                if (baseBean.getCode().equals(AppConfig.RESULT_OK)) {
                    onResponseOK(response, id);
                } else {
                    if (requestModle.isAutoDealOtherCase()) {
                        Toast.makeText(requestModle.getC(), baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        onResponseOtherCase(response, id);
                    }
                }
            } else {
//                if (requestModle.isShowError())
//                    Toast.makeText(requestModle.getC(), requestModle.getC().getResources()
//                            .getString(R.string.success_dataempty), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 成功的情况
     *
     * @param response
     * @param id
     */
    public void onResponseOK(T response, int id) {

    }

    /**
     * 其他情况
     *
     * @param response
     * @param id
     */
    public void onResponseOtherCase(T response, int id) {

    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        //关闭加载对话框
        UiHelper.getInstance().dismissLoading();
    }
}
