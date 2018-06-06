package com.ywb.tuyue.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.GameBean;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.web.WebActivity;
import com.ywb.tuyue.utils.LoadImage;
import com.ywb.tuyue.widget.BGABanner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author penghao
 * @date 2018/4/23
 * Description: 电玩城适配器
 **/
public class GameCityAdapter extends BaseRecycleViewAdapter<GameBean.ResultBean.GamesBean> {
    final int GAME_TOP = 1;
    final int GAME_NORMAL = 2;
    private View mHeaderView;

    float height;

    private List<GameBean.ResultBean.AdsBean> adsBeans = new ArrayList<>();

    public void setAdsBeans(List<GameBean.ResultBean.AdsBean> adsBeans) {
        this.adsBeans = adsBeans;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public GameCityAdapter(Context c, List<GameBean.ResultBean.GamesBean> mList, float height) {
        super(c, mList);
        this.height = height;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GAME_TOP) {
            return new GameTopViewHolder(mHeaderView);
        }
        View contentView = resIdToView(parent, R.layout.item_game_normal);
        ViewGroup.LayoutParams params = contentView.getLayoutParams();
        params.height = (int) height;
        contentView.setLayoutParams(params);
        return new GameNormalViewHolder(contentView);
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return GAME_NORMAL;
        if (position == 0) return GAME_TOP;
        return GAME_NORMAL;
    }

    private class GameTopViewHolder extends BaseViewHolder implements BGABanner.Adapter, BGABanner.OnItemClickListener {
        BGABanner bgaBanner;

        public GameTopViewHolder(View itemView) {
            super(itemView);
            bgaBanner = itemView.findViewById(R.id.banner);
        }

        @Override
        public void onBind() {
            if (adsBeans != null && !adsBeans.isEmpty()) {
                bgaBanner.setData(adsBeans, null);
                bgaBanner.setAdapter(this);
                bgaBanner.setOnItemClickListener(this);
            }
        }

        @Override
        public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
            String img = ((GameBean.ResultBean.AdsBean) model).getImg();
            LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(img), (ImageView) view);
        }

        @Override
        public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
            String title = ((GameBean.ResultBean.AdsBean) model).getTitle();
            String content = ((GameBean.ResultBean.AdsBean) model).getContent();
            Count lastCount = CountDao.getInstance().getLastCount();
            if (lastCount != null) {
                lastCount.setGameAd(lastCount.getGameAd() + 1);
            }
            CountDao.getInstance().updateCount(lastCount);
            goWeb(title, content, "game");
        }
    }

    private void goWeb(String title, String content, String type) {
        Intent intent = new Intent(c, WebActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        c.startActivity(intent);
    }

    private class GameNormalViewHolder extends BaseViewHolder {
        TextView content;
        ImageView pic;

        public GameNormalViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.game_pic);
            content = itemView.findViewById(R.id.game_text);
        }

        @Override
        public void onBind() {
            GameBean.ResultBean.GamesBean gamesBean = mList.get(position - 1);
            if (gamesBean != null) {
                String localUrl = MyDownLoadManager.getLocalUrl(gamesBean.getImg());
                Log.d("onBind-->", "getImg: " + gamesBean.getImg());
                Log.d("onBind-->", "localUrl: " + localUrl);
                LoadImage.loadImageNormal(localUrl, pic);
                content.setText(gamesBean.getName());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == GAME_TOP ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
}
