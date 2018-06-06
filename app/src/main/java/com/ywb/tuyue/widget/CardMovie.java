package com.ywb.tuyue.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.ui.movie.CardMovieInterface;
import com.ywb.tuyue.utils.LoadImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mhdt on 2017/12/16.
 * 电影卡片
 */
public class CardMovie extends CardMovieInterface {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.pay)
    ImageView pay;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.intrduce)
    TextView intrduce;

    public CardMovie(@NonNull Context context) {
        this(context, null);
    }

    public CardMovie(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardMovie(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.card_movie, this, true);
        ButterKnife.bind(view);
    }

    @Override
    public void setMovieBg(int resId) {
        img.setImageResource(resId);
    }

    @Override
    public void setMovieBg(String imageUrl) {
        LoadImage.loadImageNormal(imageUrl, img);
    }

    @Override
    public void setMovieName(String name) {
        title.setText(name);
    }

    @Override
    public void setMovieIntroduction(String introduction) {
        if (!TextUtils.isEmpty(introduction)) {
            intrduce.setText(introduction);
        }
    }

    @Override
    public void setMovieIsPay(boolean isPay) {
//        pay.setVisibility(isPay ? VISIBLE : GONE);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
