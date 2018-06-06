package com.ywb.tuyue.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ywb.tuyue.utils.UiHelper;
import com.ywb.tuyue.widget.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mhdt on 2017/12/14.
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected View mView;
    Unbinder unbinder;
    LoadingDialog dialog;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getViewId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        setHeader();
        initView(savedInstanceState);
        return mView;
    }

    @Override
    public void onDestroyView() {
        UiHelper.getInstance().destoryDialog();
        unbinder.unbind();
        super.onDestroyView();
    }

    protected abstract int getViewId();

    protected abstract void initView(Bundle savedInstanceState);

    protected <T extends View> T findViewById(int id) {
        return mView.findViewById(id);
    }

    protected void setHeader() {
    }


    /**
     * 开始帧动画
     */
    public void showLoadingDialog() {
        dialog = new LoadingDialog(mActivity);
        dialog.showDialog();
    }

    /**
     * 停止帧动画
     */
    public void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.dismissDialog();
        }
    }
}
