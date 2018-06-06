package com.ywb.tuyue.ui.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.GameCityAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.GameBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.widget.HeaderView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by penghao on 2018/4/23.
 * description：电玩城
 */

public class GameActivity extends BaseActivity {
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.game_rv)
    RecyclerView gameRv;

    GameCityAdapter adapter;
    GridLayoutManager gridLayoutManager;

    List<GameBean.ResultBean.GamesBean> gamesBeans = new ArrayList<>();
    private long lastMill;

    @Override
    protected int getViewId() {
        return R.layout.activity_game;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();
        gameRv.post(new Runnable() {
            @Override
            public void run() {
                int height = gameRv.getHeight();
                gameRv.setHasFixedSize(true);
                gameRv.setLayoutManager(gridLayoutManager = new GridLayoutManager(mContext, 4));
                gameRv.setAdapter(adapter = new GameCityAdapter(mContext, gamesBeans, height / 2.5f));
                setHeader(gameRv);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int pos) {
                        GameBean.ResultBean.GamesBean gamesBean = gamesBeans.get(pos - 1);
                        unZipAndGO(gamesBean.getGame_zip(), gamesBean.getName(), gamesBean.getName());
                    }
                });
                getGameBannerAndZip();
            }
        });

    }

    private void getGameBannerAndZip() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.getGames)//
                .build()//
                .execute(new AppGsonCallback<GameBean>(new RequestModel(mContext)) {
                    @Override
                    public void onResponseOK(GameBean response, int id) {
                        super.onResponseOK(response, id);
                        GameBean.ResultBean temp = response.getResult();
                        gamesBeans.clear();
                        if (temp != null) {
                            List<GameBean.ResultBean.GamesBean> tempList = temp.getGames();
                            if (tempList != null && !tempList.isEmpty()) {
                                gamesBeans.addAll(tempList);
                            }
                            adapter.setAdsBeans(temp.getAds());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }


    private void unZipAndGO(String thirdUrl, String indexPath, String title) {
        goHtml(title, thirdUrl);
    }

    private void goHtml(String title, String content) {
        Intent intent = new Intent(mContext, ThirdPartyActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    //flie：要删除的文件夹的所在位置
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }


    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(U.getString(R.string.game_city_title));
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        OkHttpUtils.getInstance().cancelTag(this);
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setGameTime(lastCount.getGameTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        super.onDestroy();
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.item_game_top, view, false);
        adapter.setHeaderView(header);
    }
}
