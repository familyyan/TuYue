package com.ywb.tuyue.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ywb.tuyue.R;

/**
 * Created by penghao on 2018/1/4.
 * description：
 */

public class LoadImage {

    /**
     * 加载网络图片
     *
     * @param url       图片地址
     * @param imageView 容器
     */
    public static void loadImageAnimate(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(U.getAppContext())
                .load(url)
                .placeholder(R.drawable.image_loading)//加载中的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//展示在控件中大小图片尺寸和原图都会缓存
                .error(R.drawable.image_loading)//加载失败的图片
                .into(imageView);
    }

    /**
     * 加载网络图片
     *
     * @param url       图片地址
     * @param imageView 容器
     */
    public static void loadImageNormal(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(U.getAppContext())
                .load(url)
                .placeholder(R.drawable.image_loading)//加载中的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//展示在控件中大小图片尺寸和原图都会缓存
                .error(R.drawable.image_loading)//加载失败的图片
                .into(imageView);
    }

    /**
     * 加载网络图片
     *
     * @param url       图片地址
     * @param imageView 容器
     */
    public static void loadImageAdvert(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(U.getAppContext())
                .load(url)
                .placeholder(R.drawable.image_loading)//加载中的图片
                .error(R.drawable.bg_unlock)//加载失败的图片
                .into(imageView);
    }
}
