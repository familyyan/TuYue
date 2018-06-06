package com.ywb.tuyue.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.MovieScanAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.Movie;
import com.ywb.tuyue.bean.MoviesListBean;
import com.ywb.tuyue.db.dao.MovieDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.U;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ph on 18-1-15.
 */

public class LocalVideoFrag extends BaseFragment {
    RecyclerView recyclerview;
    SmartRefreshLayout refreshLayout;
    LinearLayout emptyView;
    MovieScanAdapter movieScanAdapter;
    List<Movie.ResultBean> movieList;
    int typeInt;

    @Override
    protected int getViewId() {
        return R.layout.frag_local;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        resolverIntent();
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerview = findViewById(R.id.recyclerview);
        emptyView = findViewById(R.id.empty_view);
        movieList = new ArrayList<>();
        recyclerview.post(() -> {
                    int height = recyclerview.getHeight();
                    recyclerview.setLayoutManager(new GridLayoutManager(mActivity, 6));
                    recyclerview.setHasFixedSize(true);
                    recyclerview.setAdapter(movieScanAdapter = new MovieScanAdapter(mActivity, movieList, height / 2));
                    movieScanAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int pos) {
                            Intent intent = new Intent();
                            intent.putExtra("movieBean", movieList.get(pos));
                            intent.putExtra("isLocal", true);
                            MovieActivity.Go(mActivity, MovieActivity.class, intent);
                        }
                    });
                    //---------------
                    ClassicsHeader classicsHeader = new ClassicsHeader(mActivity);
                    classicsHeader.getTitleText().setTextColor(U.getColor(R.color.white));
                    classicsHeader.getLastUpdateText().setTextColor(U.getColor(R.color.white));
                    refreshLayout.setRefreshHeader(classicsHeader);
                    refreshLayout.setEnableLoadmore(false);
                    refreshLayout.setEnableRefresh(true);

                    refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                        @Override
                        public void onRefresh(RefreshLayout refreshlayout) {
                            refreshLayout.finishRefresh();
                        }
                    });
                    loadData();
                    refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
                }
        );
        //点击重新加载
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.finishRefresh();
            }
        });
    }

    private void resolverIntent() {
        typeInt = getArguments().getInt("typeId");
    }

    private void setEmptyViewShow(boolean isShow) {
        emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        List<MoviesListBean> localList;
        if (typeInt == 0) {//根据type查询每个栏目对应的电影
            localList = MovieDao.getInstance().getMovieList();
        } else if (typeInt == -1) {//如果为-1则显示1905电影
            localList = MovieDao.getInstance().getMoviesByType(AppConfig.TYPE_ID_1905);
        } else {//如果为空则默认显示全部电影
            localList = MovieDao.getInstance().getMoviesByType(typeInt + "");
        }
        movieList.clear();
        if (localList != null && !localList.isEmpty()) {
            for (int i = 0; i < localList.size(); i++) {
                MoviesListBean moviesListBean = localList.get(i);
                Movie.ResultBean resultBean = new Movie.ResultBean(localList.get(i).getProfile(), localList.get(i).getImagePath(), localList.get(i).getName(), localList.get(i).getFile_path(), localList.get(i).getActor(), localList.get(i).getDirector(), localList.get(i).getFile_path());
                String localVideoUrl;
                if (TextUtils.equals(moviesListBean.getType_id(), AppConfig.TYPE_ID_1905)) {
//                    localVideoUrl = MyDownLoadManager.getLocalUrl(StringUtils.dealStr("http://139.196.86.51:8088/data/micro_ticket/dst/gao_tie/480p_2/319d48a27491d948ef288ce7664a03c0.mp4"));
                    localVideoUrl = AppConfig.DEFAULT_SAVE_PATH + moviesListBean.getName() + ".ts";
                } else {
                    localVideoUrl = MyDownLoadManager.getLocalUrl(moviesListBean.getDownload_url());
                }
                String localImgUrl = MyDownLoadManager.getLocalUrl(moviesListBean.getPoster_url());

                resultBean.setDownload_url(localVideoUrl);
                resultBean.setPoster_url(localImgUrl);
                resultBean.setDetail(moviesListBean.getDetail() + "");

                movieList.add(resultBean);
            }
            Collections.reverse(movieList);//倒叙
            setEmptyViewShow(false);
        } else {
            setEmptyViewShow(true);
        }
        refreshLayout.finishRefresh();
        movieScanAdapter.notifyDataSetChanged();
    }

}
