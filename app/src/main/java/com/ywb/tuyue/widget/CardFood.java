package com.ywb.tuyue.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.food.OnAddOneClickListener;
import com.ywb.tuyue.utils.LoadImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mhdt on 2017/12/18.
 */
public class CardFood extends FrameLayout {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.miaoshu)
    TextView miaoshu;
    @BindView(R.id.price)
    TextView price;
    private Context context;
    private OnAddOneClickListener onAddOneClickListener;

    public void setOnAddOneClickListener(OnAddOneClickListener onAddOneClickListener) {
        this.onAddOneClickListener = onAddOneClickListener;
    }

    public CardFood(@NonNull Context context) {
        this(context, null);
    }

    public CardFood(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardFood(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.card_food, this, true);
        ButterKnife.bind(view);
        //  view.findViewById(R.id.add_one);
    }

    @OnClick(R.id.add_one)
    public void onViewClicked() {
        onAddOneClickListener.onAddOneClickListener();
    }

    public void setImage(String url) {
        String localPath = MyDownLoadManager.getLocalUrl(url);
        LoadImage.loadImageNormal(localPath, image);
    }

    public void setName(String str) {
        name.setText(str);
    }

    public void setMiaoshu(String str) {
        if (!TextUtils.isEmpty(str)) {
            miaoshu.setText(str);
            miaoshu.setVisibility(VISIBLE);
        } else {
            miaoshu.setVisibility(GONE);
        }
    }

    public void setPrice(String str) {
        price.setText(str);
    }
}
