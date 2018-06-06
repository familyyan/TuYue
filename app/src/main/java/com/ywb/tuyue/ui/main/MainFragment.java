package com.ywb.tuyue.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.CardBean;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.HomeBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.MyUrls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.ui.city.CityListActivity;
import com.ywb.tuyue.ui.food.FoodOlderActivity;
import com.ywb.tuyue.ui.movie.MovieTheaterActivity;
import com.ywb.tuyue.ui.setting.SettingActivity;
import com.ywb.tuyue.ui.subway.SuburbanStyleActivity;
import com.ywb.tuyue.ui.web.BookActivity;
import com.ywb.tuyue.ui.web.GameActivity;
import com.ywb.tuyue.ui.web.MusicActivity;
import com.ywb.tuyue.ui.web.ThirdPartyActivity;
import com.ywb.tuyue.ui.web.WebActivity;
import com.ywb.tuyue.utils.LoadImage;
import com.ywb.tuyue.widget.Card;
import com.ywb.tuyue.widget.HeaderView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mhdt on 2017/12/14.
 * 首页
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.iv_parentView)
    LinearLayout parentView;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;

    int[] cardIds = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.cityList, R.id.card7};
    Class[] cardToIntent = {MovieTheaterActivity.class, GameActivity.class, MusicActivity.class, FoodOlderActivity.class,
            BookActivity.class, CityListActivity.class, SuburbanStyleActivity.class};

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;

    private static List<CardBean> cardBeans = new ArrayList<>();
    private List<HomeBean.ResultBean.AdvertBean> advertBeans = new ArrayList<>();//广告
    private List<HomeBean.ResultBean.HotMovieBean> movieBeans = new ArrayList<>();//电影

    private String advertTitle01, advertTitle02;
    private String advertContent01, advertContent02;


    @Override
    protected int getViewId() {
        return R.layout.frag_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initView(Bundle savedInstanceState) {
        initCardListener();
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();//检查权限
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(MyUrls.homePage)//
//                .addHeader(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .build()//
                .execute(new AppGsonCallback<HomeBean>(new RequestModel(mActivity).setShowProgress(false).setShowError(false)) {
                    @Override
                    public void onResponseOK(HomeBean response, int id) {
                        super.onResponseOK(response, id);
                        advertBeans.clear();
                        movieBeans.clear();
                        cardBeans.clear();
                        HomeBean.ResultBean result = response.getResult();
                        if (result != null) {
                            List<HomeBean.ResultBean.AdvertBean> temp = result.getAdvert();
                            List<HomeBean.ResultBean.HotMovieBean> hotMovie = result.getHotMovie();
                            advertBeans.addAll(temp);
                            movieBeans.addAll(hotMovie);
                            if (advertBeans.size() > 1) {
                                if (iv01 == null || iv02 == null) {
                                    return;
                                }
                                advertTitle01 = advertBeans.get(0).getTitle();
                                advertContent01 = advertBeans.get(0).getContentPd();

                                advertTitle02 = advertBeans.get(1).getTitle();
                                advertContent02 = advertBeans.get(1).getContentPd();

                                LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(advertBeans.get(0).getImg_url()), iv01);
                                LoadImage.loadImageNormal(MyDownLoadManager.getLocalUrl(advertBeans.get(1).getImg_url()), iv02);
                            }
                            if (!movieBeans.isEmpty()) {
                                HomeBean.ResultBean.HotMovieBean hotMovieBean = movieBeans.get(0);
                                cardBeans.add(new CardBean(MyDownLoadManager.getLocalUrl(hotMovieBean.getPoster_url()), hotMovieBean.getProfile(), hotMovieBean.getName()));
                                cardBeans.add(new CardBean(R.drawable.pic44, "Game", "电玩"));
                                cardBeans.add(new CardBean(R.drawable.pic66, R.drawable.icon_music, "Music", "音乐"));
                                cardBeans.add(new CardBean(R.drawable.pic55, R.drawable.icon_order, "Order", "点餐"));
                                cardBeans.add(new CardBean(R.drawable.pic77, R.drawable.icon_book, "Book", "书吧"));
                                cardBeans.add(new CardBean(R.drawable.pic88, "City", "城市"));
                                cardBeans.add(new CardBean(R.drawable.pic99, "Suburban style", "成铁风采"));
                                for (int i = 0; i < cardIds.length; i++) {
                                    Card card = findViewById(cardIds[i]);
                                    card.setCardBean(cardBeans.get(i));
                                }
                            }
                        }

                    }
                });
    }


    /**
     * 检查权限
     */
    private void checkPermission() {
        mPermissionList.clear();
        /**
         * 判断哪些权限未授予
         */
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(mActivity, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
//                delayEntryPage();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initCardListener() {
        View.OnClickListener cardOnClickListener = v -> {
            int id = (int) v.getTag();
            Count lastCount = CountDao.getInstance().getLastCount();
            if (lastCount != null) {
                switch (id) {
                    case 0:
                        lastCount.setMovies(lastCount.getMovies() + 1);
                        break;
                    case 1:
                        lastCount.setGame(lastCount.getGame() + 1);
                        break;
                    case 2:
                        lastCount.setMusic(lastCount.getMusic() + 1);
                        break;
                    case 3:
                        lastCount.setFood(lastCount.getFood() + 1);
                        break;
                    case 4:
                        lastCount.setBook(lastCount.getBook() + 1);
                        break;
                    case 5:
                        lastCount.setCity(lastCount.getCity() + 1);
                        break;
                    case 6:
                        lastCount.setSubway(lastCount.getSubway() + 1);
                        break;
                }
            }
            CountDao.getInstance().updateCount(lastCount);
            MovieTheaterActivity.Go(mActivity, cardToIntent[id]);
        };

        for (int i = 0; i < cardIds.length; i++) {
            View v = findViewById(cardIds[i]);
            v.setTag(i);
            v.setOnClickListener(cardOnClickListener);
        }
    }

    @Override
    protected void setHeader() {
        super.setHeader();
        header.setLeftBtnVisiable(View.GONE);
        header.setImgTitleOnly(R.drawable.main_title);
        header.setRightBtnClickListsner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.Go(mActivity, SettingActivity.class);
            }
        });
    }

    @OnClick({R.id.iv_01, R.id.iv_02})
    public void onViewClicked(View view) {
        Count lastCount = CountDao.getInstance().getLastCount();
        switch (view.getId()) {
            case R.id.iv_01:
                if (lastCount != null) {
                    lastCount.setHomeAd1(lastCount.getHomeAd1() + 1);
                }
                goWeb(advertTitle01, advertContent01, "home1");
                break;
            case R.id.iv_02:
                if (lastCount != null) {
                    lastCount.setHomeAd2(lastCount.getHomeAd2() + 1);
                }
                goWeb(advertTitle02, advertContent02, "home2");
                break;
        }
        CountDao.getInstance().updateCount(lastCount);
    }

    private void goWeb(String title, String content, String type) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void goHtml(String title, String content) {
        Intent intent = new Intent(mActivity, ThirdPartyActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(this);
        header.unRegister();
        super.onDestroyView();
    }

}
