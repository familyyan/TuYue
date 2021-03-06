package com.ywb.tuyue.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;

/**
 * Created by mhdt on 2017/12/14.
 * 标题
 */
public class HeaderView extends FrameLayout {
    private Context mContext;

    ImageButton btnLeft;
    TextView tvTitle;
    ImageView ivTitle;
    ImageButton btnRight;
    MyCustomStatusBar statusLine;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.header, this, true);
        btnLeft = findViewById(R.id.btn_left);
        tvTitle = findViewById(R.id.tv_title);
        ivTitle = findViewById(R.id.iv_title);
        btnRight = findViewById(R.id.btn_right);
        statusLine = findViewById(R.id.status_bar);
//        StatusBarUtil.immersive((Activity) mContext);
//        StatusBarUtil.buildStatuSpace(mContext, statusLine);
    }

    /**
     * 设置左侧按钮图片
     *
     * @param imgRes
     */
    public void setLeftBtnSrc(int imgRes) {
        btnLeft.setImageResource(imgRes);
    }

    /**
     * 设置左侧按钮点击事件
     *
     * @param onClickListener
     */
    public void setLeftBtnClickListsner(OnClickListener onClickListener) {
        btnLeft.setOnClickListener(onClickListener);
    }

    /**
     * 设置左侧按钮的图标和点击事件
     *
     * @param imgRes
     * @param onClickListener
     */
    public void setLeftBtnProperty(int imgRes, OnClickListener onClickListener) {
        setLeftBtnSrc(imgRes);
        setLeftBtnClickListsner(onClickListener);
    }

    /**
     * 设置左侧按钮可见性
     *
     * @param visiable
     */
    public void setLeftBtnVisiable(int visiable) {
        btnLeft.setVisibility(visiable);
    }

    /**
     * 设置右侧按钮图片
     *
     * @param imgRes
     */
    public void setRightBtnSrc(int imgRes) {
        btnRight.setImageResource(imgRes);
    }

    /**
     * 设置右侧按钮点击事件
     *
     * @param onClickListener
     */
    public void setRightBtnClickListsner(OnClickListener onClickListener) {
        btnRight.setOnClickListener(onClickListener);
    }

    /**
     * 设置右侧按钮可见性
     *
     * @param visiable
     */
    public void setRightBtnVisiable(int visiable) {
        btnRight.setVisibility(visiable);
    }

    /**
     * 设置右侧按钮的图标和点击事件
     *
     * @param imgRes
     * @param onClickListener
     */
    public void setRightBtnProperty(int imgRes, OnClickListener onClickListener) {
        setRightBtnSrc(imgRes);
        setRightBtnClickListsner(onClickListener);
    }

    /**
     * 设置标题
     *
     * @param textTitleRes
     */
    public void setTitle(int textTitleRes) {
        tvTitle.setText(textTitleRes);
    }

    /**
     * 设置标题
     *
     * @param textTitle
     */
    public void setTitle(String textTitle) {
        tvTitle.setText(textTitle);
    }

    /**
     * 设置图片标题
     *
     * @param imgTitleRes
     */
    public void setImgTitle(int imgTitleRes) {
        ivTitle.setImageResource(imgTitleRes);
    }

    /**
     * 设置标题隐藏其它类别标题
     *
     * @param textTitleRes
     */
    public void setTitleOnly(int textTitleRes) {
        setTitle(textTitleRes);
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
    }

    /**
     * 设置图片标题隐藏其它类别标题
     *
     * @param imgTitleRes
     */
    public void setImgTitleOnly(int imgTitleRes) {
        setImgTitle(imgTitleRes);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
    }

    public void unRegister() {
        statusLine.unregisterBroadcast();
    }
}
