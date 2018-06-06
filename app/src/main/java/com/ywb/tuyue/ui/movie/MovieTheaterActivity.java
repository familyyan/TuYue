package com.ywb.tuyue.ui.movie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.MenuAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.MovieMenuBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.widget.CustomViewPager;
import com.ywb.tuyue.widget.HeaderView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mhdt on 2017/12/14.
 * 影院
 */
public class MovieTheaterActivity extends BaseActivity {
    @BindView(R.id.movieMenu)
    RecyclerView movieMenu;

    @BindView(R.id.header)
    HeaderView header;

    CustomViewPager movePager;
    MenuAdapter menuAdapter;

    private int currentPosition;
    List<LocalVideoFrag> movieScanFragList = new ArrayList<>();

    List<MovieMenuBean.ResultBean> movieMenuBeans = new ArrayList<>();

    private int page = 1, pageSize = 100;
    private long lastMill;

    @Override
    protected int getViewId() {
        return R.layout.activity_movietheater;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();
        movePager = findViewById(R.id.movePager);
        movePager.setOffscreenPageLimit(0);
        initMenu();
        initData();
    }

    private void initData() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.getMoviesConfigTypeList)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams(AppConfig.page, page + "")//
                .addParams(AppConfig.pageSize, pageSize + "")//
                .build()//
                .execute(new AppGsonCallback<MovieMenuBean>(new RequestModel(mContext).setShowProgress(false)) {
                    @Override
                    public void onResponseOK(MovieMenuBean response, int id) {
                        super.onResponseOK(response, id);
                        List<MovieMenuBean.ResultBean> result = response.getResult();
                        if (result != null && !result.isEmpty()) {
                            movieMenuBeans.addAll(result);
                        }
                        menuAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        movieMenuBeans.clear();
                        movieMenuBeans.add(new MovieMenuBean.ResultBean("本地视频", -2));
                        movieMenuBeans.add(new MovieMenuBean.ResultBean("电影", -3));
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        initMovie();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        super.onError(call, e, id);
                        Toast.makeText(mContext, "没有连接网络，只能看本地视频！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        header.setTitleOnly(R.string.movie_title);
        header.setRightBtnVisiable(View.GONE);
        header.setLeftBtnClickListsner(onBackClickListener);
    }

    private void initMenu() {
        menuAdapter = new MenuAdapter(mContext, movieMenuBeans, currentPosition);
        movieMenu.setLayoutManager(new LinearLayoutManager(mContext));
        movieMenu.setHasFixedSize(true);
        movieMenu.setAdapter(menuAdapter);
        menuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                currentPosition = pos;
                menuAdapter.setCheckedPosition(currentPosition);
                movePager.setCurrentPage(currentPosition);
            }
        });
    }

    private void initMovie() {
        //添加本地视频
        for (int i = 0; i < movieMenuBeans.size(); i++) {
            LocalVideoFrag localFrag = new LocalVideoFrag();
            Bundle bundle = new Bundle();
            int typeId;
            if (i == 0) {//本地所有电影
                typeId = 0;
            } else if (i == 1) {//1905电影
                typeId = -1;
            } else {
                typeId = movieMenuBeans.get(i).getId();
            }
            bundle.putInt("typeId", typeId);
            localFrag.setArguments(bundle);
            movieScanFragList.add(localFrag);
        }
        movePager.setScanScroll(false);
        movePager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return movieScanFragList.get(position);
            }

            @Override
            public int getCount() {
                return movieScanFragList.size();
            }
        });
    }

    @Override
    protected void onDestroy() {
        header.unRegister();
        OkHttpUtils.getInstance().cancelTag(this);
        int staySecond = DateUtils.getStaySecond(lastMill, System.currentTimeMillis());
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setMoviesTime(lastCount.getMoviesTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        super.onDestroy();
    }
}
