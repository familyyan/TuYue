package com.ywb.tuyue.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.ywb.tuyue.bean.CardBean;
import com.ywb.tuyue.db.DataBase;

/**
 * Created by mhdt on 2017/12/14.
 * 首页卡片接口
 */

public abstract class Card extends FrameLayout {
    public Card(@NonNull Context context) {
        super(context);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置背景图片
     *
     * @param url
     */
    abstract void setCardBg(String url);

    /**
     * 设置背景图片
     *
     * @param resId
     */
    abstract void setCardBg(int resId);


    /**
     * 设置图标
     *
     * @param iconId
     */
    abstract void setCardIcon(int iconId);

    /**
     * 设置英字标题
     *
     * @param ENText
     */
    abstract void setENText(String ENText);

    /**
     * 设置汉字标题
     *
     * @param ZNText
     */
    abstract void setZNText(String ZNText);

    public void setCardBean(CardBean cardBean) {
        if (cardBean.getUrl() != null) {
            Log.d("setCardBg", "___" + DataBase.getString(cardBean.getUrl()));
            setCardBg(cardBean.getUrl());
        } else {
            setCardBg(cardBean.getResId());
        }
        setCardIcon(cardBean.getIconId());
        setENText(cardBean.getENText() + "");
        setZNText(cardBean.getZNText() + "");
    }
}
