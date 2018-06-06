package com.ywb.tuyue.ui.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.BookBean;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.utils.DateUtils;
import com.ywb.tuyue.utils.LoadImage;
import com.ywb.tuyue.utils.U;
import com.ywb.tuyue.widget.BGABanner;
import com.ywb.tuyue.widget.CustomViewPager;
import com.ywb.tuyue.widget.HeaderView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by penghao on 2018/4/24.
 * description：书吧
 */

public class BookActivity extends BaseActivity implements BGABanner.Adapter, BGABanner.OnItemClickListener {
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.banner)
    BGABanner bgaBanner;
    @BindView(R.id.pagerTab)
    TabLayout pagerTab;
    @BindView(R.id.pager)
    CustomViewPager pager;

    List<BookListFrag> bookListFrags = new ArrayList<>();

    List<BookBean.ResultBean.BooksBeanX> bookMenuList = new ArrayList<>();
    List<BookBean.ResultBean.AdsBean> bannerUrls = new ArrayList<>();
    private long lastMill;

    @Override
    protected int getViewId() {
        return R.layout.activity_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lastMill = System.currentTimeMillis();
        getData();
    }

    private void getData() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.getBooks)//
                .build()//
                .execute(new AppGsonCallback<BookBean>(new RequestModel(mContext)) {
                    @Override
                    public void onResponseOK(BookBean response, int id) {
                        super.onResponseOK(response, id);
                        BookBean.ResultBean result = response.getResult();
                        bannerUrls.clear();
                        bookMenuList.clear();
                        if (result != null) {
                            List<BookBean.ResultBean.AdsBean> tempAds = result.getAds();
                            if (tempAds != null && !tempAds.isEmpty()) {
                                bannerUrls.addAll(tempAds);
                            }
                            List<BookBean.ResultBean.BooksBeanX> books = result.getBooks();
                            if (books != null && !books.isEmpty()) {
                                bookMenuList.addAll(books);
                            }
                        }
                        setBanner();
                        setViewPager();
                    }
                });

    }

    private void setBanner() {
        if (bannerUrls != null && !bannerUrls.isEmpty()) {
            bgaBanner.setData(bannerUrls, null);
        }
        bgaBanner.setAdapter(this);
        bgaBanner.setOnItemClickListener(this);
    }

    private void setViewPager() {
        bookListFrags.clear();
        for (int i = 0; i < bookMenuList.size(); i++) {
            Bundle bundle = new Bundle();
            BookListFrag scanFrag = new BookListFrag();
            bundle.putSerializable("bookList", bookMenuList.get(i));
            scanFrag.setArguments(bundle);
            bookListFrags.add(scanFrag);
        }
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return bookListFrags.get(position);
            }

            @Override
            public int getCount() {
                return bookListFrags.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return bookMenuList.get(position).getName();
            }
        });
        //----
        for (int i = 0; i < bookMenuList.size(); i++) {
            if (i == 0) {
                pagerTab.addTab(pagerTab.newTab().setText(bookMenuList.get(i).getName()), true);
            } else {
                pagerTab.addTab(pagerTab.newTab().setText(bookMenuList.get(i).getName()));
            }
        }
        pagerTab.setupWithViewPager(pager);
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setTitle(U.getString(R.string.book_title));
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
            lastCount.setBookTime(lastCount.getBookTime() + staySecond);
            CountDao.getInstance().updateCount(lastCount);
        }
        super.onDestroy();
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        String img = ((BookBean.ResultBean.AdsBean) model).getImg();
        LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(img), (ImageView) view);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
        String title = ((BookBean.ResultBean.AdsBean) model).getTitle();
        String content = ((BookBean.ResultBean.AdsBean) model).getContent();
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            lastCount.setBookAd(lastCount.getBookAd() + 1);
        }
        CountDao.getInstance().updateCount(lastCount);
        goWeb(title, content, "book");
    }

    private void goWeb(String title, String content, String type) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
