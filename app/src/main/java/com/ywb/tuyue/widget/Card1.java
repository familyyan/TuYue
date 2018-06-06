package com.ywb.tuyue.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.utils.LoadImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mhdt on 2017/12/14.
 * 首页卡片类型1
 */

public class Card1 extends Card {
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.en_name)
    TextView enName;
    @BindView(R.id.zn_name)
    TextView znName;

    public Card1(@NonNull Context context) {
        this(context, null);
    }

    public Card1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Card1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.card_1, this, true);
        ButterKnife.bind(view);
    }

    @Override
    public void setCardBg(String url) {
//        LoadImage.loadImageAnimate(url, bg);
//        bg.setImageURI(Uri.fromFile(new File(DataBase.getString(url))));
        LoadImage.loadImageNormal(url, bg);
    }

    @Override
    public void setCardBg(int resId) {
        bg.setImageResource(resId);
    }

    @Override
    public void setCardIcon(int iconId) {

    }

    @Override
    public void setENText(String ENText) {
        znName.setText(ENText);
    }

    @Override
    public void setZNText(String ZNText) {
        enName.setText(ZNText);
    }
}
