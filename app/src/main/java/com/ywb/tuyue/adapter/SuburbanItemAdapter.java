package com.ywb.tuyue.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.ChengTie;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.utils.LoadImage;

import java.util.List;

/**
 * Created by penghao on 2017/12/22.
 * description：
 */

public class SuburbanItemAdapter extends BaseRecycleViewAdapter<ChengTie.ResultBean.ArticleListBean> {
    float height;

    public SuburbanItemAdapter(Context c, List<ChengTie.ResultBean.ArticleListBean> mList, float height) {
        super(c, mList);
        this.height = height;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = resIdToView(parent, R.layout.item_item_sub);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = (int) height;
        itemView.setLayoutParams(layoutParams);
        return new ItemSubViewHolder(itemView);
    }


    public class ItemSubViewHolder extends BaseViewHolder {
        ImageView playIcon, imageView;
        TextView title;

        public ItemSubViewHolder(View itemView) {
            super(itemView);
            playIcon = itemView.findViewById(R.id.icon_play);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void onBind() {
            ChengTie.ResultBean.ArticleListBean articleListBean = mList.get(position);
            if (articleListBean != null) {
                playIcon.setVisibility(TextUtils.equals("2", articleListBean.getContent_type()) ? View.VISIBLE : View.GONE);
                title.setText(articleListBean.getTitle());

                LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(articleListBean.getImg_url()), imageView);
                //            imageView.setImageURI(Uri.fromFile(new File(DataBase.getString(articleListBean.getImg_url()))));
            }

        }
    }

}
