package com.ywb.tuyue.ui.movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ywb.tuyue.bean.Movie;

/**
 * Created by penghao on 2017/12/26.
 * description：
 */

public abstract class CardMovieInterface extends FrameLayout {
    public CardMovieInterface(@NonNull Context context) {
        super(context);
    }

    public CardMovieInterface(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardMovieInterface(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置背景
     *
     * @param resId 资源id
     */
    protected abstract void setMovieBg(int resId);

    /**
     * 设置背景
     *
     * @param imageUrl 图片地址
     */
    protected abstract void setMovieBg(String imageUrl);

    /**
     * 名称
     *
     * @param name
     */
    protected abstract void setMovieName(String name);

    /**
     * 简介
     *
     * @param introduction
     */
    protected abstract void setMovieIntroduction(String introduction);

    /**
     * 是否付费
     *
     * @param isPay
     */
    protected abstract void setMovieIsPay(boolean isPay);

    public void setMovieBean(Movie.ResultBean movieBean) {
        setMovieBg(movieBean.getPoster_url());
        setMovieName(movieBean.getName());
        setMovieIntroduction(movieBean.getProfile());
//        setMovieIsPay(movieBean.isPay());
    }
}
