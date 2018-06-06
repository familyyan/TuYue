package com.ywb.tuyue.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.ChengTie;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.city.CityDetailsActivity;
import com.ywb.tuyue.ui.subway.VideoActivity;

import java.util.List;

/**
 * Created by penghao on 2017/12/22.
 * description：城铁风采
 */

public class SuburbanAdapter extends BaseRecycleViewAdapter<ChengTie.ResultBean> {
    float height;

    public SuburbanAdapter(Context c, List<ChengTie.ResultBean> mList, float height) {
        super(c, mList);
        this.height = height;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = resIdToView(parent, R.layout.item_suburban);
        return new CityViewHolder(contentView);
    }

    private class CityViewHolder extends BaseViewHolder {
        RecyclerView recyclerView;
        SuburbanItemAdapter itemAdapter;
        View line;
        TextView mClazz;

        public CityViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_RecyclerView);
            line = itemView.findViewById(R.id.v_line);
            mClazz = itemView.findViewById(R.id.title_clazz);
        }

        @Override
        public void onBind() {
            line.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(c, 4));
            recyclerView.setAdapter(itemAdapter = new SuburbanItemAdapter(c, mList.get(position).getArticle_list(), height));
            mClazz.setText(mList.get(position).getName());
            itemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int pos) {
                    ChengTie.ResultBean.ArticleListBean articleListBean = mList.get(position).getArticle_list().get(pos);
                    //---详情---1代表图文 2代表视频
                    boolean isVideo = TextUtils.equals("2", articleListBean.getContent_type());
                    Intent intent = new Intent();
                    if (isVideo) {//去播放页面,如果是有网环境
//                        intent.putExtra("content", articleListBean.getContent_img());//有网
                        intent.putExtra("content", MyDownLoadManager.getLocalUrl(articleListBean.getContent_img()));//离线模式
                        intent.putExtra("screenshots", articleListBean.getScreenshots());
                        intent.putExtra("title", articleListBean.getTitle());
                    } else {//去文章详情页面
                        City.ResultBean resultBean = new City.ResultBean();
                        resultBean.setName(articleListBean.getTitle());
                        resultBean.setContentPd(articleListBean.getContentPd());
//                        DataBase.saveString("content_image", articleListBean.getContent());
//                        resultBean.setContent_img(articleListBean.getContent_img());
                        intent.putExtra("cityBean", resultBean);
                    }
                    VideoActivity.Go(c, isVideo ? VideoActivity.class : CityDetailsActivity.class, intent);
                }
            });
        }
    }
}
