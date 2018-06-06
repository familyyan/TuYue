package com.ywb.tuyue.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.MovieScanAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.Movie;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.U;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mhdt on 2017/12/16.
 * 电影浏览模块
 */
public class MovieScanFrag extends BaseFragment {
    RecyclerView recyclerview;
    SmartRefreshLayout refreshLayout;
    LinearLayout emptyView;

    MovieScanAdapter movieScanAdapter;
    List<Movie.ResultBean> movieList;
    private int pageIndex = 1, pageCount = 12;

    private int type;

    @Override
    protected int getViewId() {
        return R.layout.frag_moviescan;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        resolverIntent();//接收传入的type
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerview = findViewById(R.id.recyclerview);
        emptyView = findViewById(R.id.empty_view);
        movieList = new ArrayList<>();
        recyclerview.post(new Runnable() {
            @Override
            public void run() {
                int height = recyclerview.getHeight();
                recyclerview.setLayoutManager(new GridLayoutManager(mActivity, 6));
                recyclerview.setHasFixedSize(true);
                recyclerview.setAdapter(movieScanAdapter = new MovieScanAdapter(mActivity, movieList, height / 2));
                movieScanAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int pos) {
                        Intent intent = new Intent();
                        intent.putExtra("movieBean", movieList.get(pos));
                        intent.putExtra("isLocal", false);
                        MovieActivity.Go(mActivity, MovieActivity.class, intent);
                    }
                });
                //---------------
                ClassicsHeader classicsHeader = new ClassicsHeader(mActivity);
                ClassicsFooter classicsFooter = new ClassicsFooter(mActivity);
                classicsHeader.getTitleText().setTextColor(U.getColor(R.color.white));
                classicsHeader.getLastUpdateText().setTextColor(U.getColor(R.color.white));
                classicsFooter.getTitleText().setTextColor(U.getColor(R.color.white));
                refreshLayout.setRefreshHeader(classicsHeader);
                refreshLayout.setRefreshFooter(classicsFooter);
                refreshLayout.setEnableLoadmore(false);
                refreshLayout.setEnableRefresh(true);

                refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        pageIndex = 1;
                        loadData();
                    }
                });

                refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {
                        loadData();
                    }
                });
                refreshLayout.autoRefresh();
                refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
            }
        });
        //点击重新加载
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageIndex = 1;
                loadData();
            }
        });
    }

    private void resolverIntent() {
        type = getArguments().getInt("typeId");
    }

    private void setEmptyViewShow(boolean isShow) {
        emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        Observable.create(new ObservableOnSubscribe<List<Movie.ResultBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Movie.ResultBean>> e) throws Exception {
                OkHttpUtils
                        .post()//
                        .tag(this)//
                        .url(Urls.getMoviesList)
                        .addParams(AppConfig.token_key, AppConfig.token_value)//
                        .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                        .addParams(AppConfig.page, pageIndex + "")//
                        .addParams(AppConfig.pageSize, pageCount + "")//
                        .addParams("typeId", type + "")//
                        .build()//
                        .execute(new AppGsonCallback<Movie>(new RequestModel(mActivity)) {
                            @Override
                            public void onResponseOK(Movie response, int id) {
                                super.onResponseOK(response, id);
                                List<Movie.ResultBean> result = response.getResult();
                                if (result != null && !result.isEmpty()) {
                                    setEmptyViewShow(false);
                                } else {
                                    if (pageIndex == 1) {
                                        setEmptyViewShow(true);
                                    }
                                }
                                pageIndex++;
                                e.onNext(result);
                            }

                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie.ResultBean>>() {
                    @Override
                    public void accept(List<Movie.ResultBean> tempMovieList) throws Exception {
                        if (pageIndex == 2) {//refresh
                            movieList.clear();
                            refreshLayout.finishRefresh();
                        } else {//loadmore
                            refreshLayout.finishLoadmore();
                        }
                        movieList.addAll(tempMovieList);
                        movieScanAdapter.notifyDataSetChanged();

                        if (tempMovieList.size() >= pageCount) {
                            refreshLayout.setEnableLoadmore(true);
                        } else {
                            refreshLayout.setEnableLoadmore(false);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroyView();
    }
}
