package com.ywb.tuyue.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.utils.LoadImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mhdt on 2017/12/14.
 * 首页卡片类型2
 */
public class Card2 extends Card {
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.icon)
    ImageView ivIcon;
    @BindView(R.id.en_name)
    TextView enName;
    @BindView(R.id.zn_name)
    TextView znName;
    private boolean isShowIcon = true;
    private ImageView icon;

    public Card2(@NonNull Context context) {
        this(context, null);
    }

    public Card2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Card2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.card_2, this, true);
        ButterKnife.bind(view);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.card);
            isShowIcon = typedArray.getBoolean(R.styleable.card_showIcon, isShowIcon);
            typedArray.recycle();
        }
        icon = findViewById(R.id.icon);
        icon.setVisibility(isShowIcon ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setCardBg(int resId) {
        bg.setImageResource(resId);
    }

    @Override
    public void setCardBg(String url) {
        LoadImage.loadImageNormal(DataBase.getString(url), bg);
    }

    @Override
    public void setCardIcon(int iconId) {
        ivIcon.setImageResource(iconId);
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
