package com.ywb.tuyue.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.BaseBean;
import com.ywb.tuyue.bean.BookBean;
import com.ywb.tuyue.bean.ChengTie;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.DataSyncInfo;
import com.ywb.tuyue.bean.FoodMenuBean;
import com.ywb.tuyue.bean.GameBean;
import com.ywb.tuyue.bean.HomeAdvertBean;
import com.ywb.tuyue.bean.HomeBean;
import com.ywb.tuyue.bean.MovieMenuBean;
import com.ywb.tuyue.bean.MovieSyncDataBean;
import com.ywb.tuyue.bean.MoviesListBean;
import com.ywb.tuyue.bean.MusicBean;
import com.ywb.tuyue.bean.RequestModel;
import com.ywb.tuyue.bean.RichTextBean;
import com.ywb.tuyue.bean.Videos1905Bean;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.db.SharedPreferenceHelper;
import com.ywb.tuyue.db.dao.BannerDao;
import com.ywb.tuyue.db.dao.CountDao;
import com.ywb.tuyue.db.dao.MovieDao;
import com.ywb.tuyue.downLoad.DownLoadEntity;
import com.ywb.tuyue.downLoad.DownLoadTask;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.net.AppGsonCallback;
import com.ywb.tuyue.net.GsonSerializator;
import com.ywb.tuyue.net.Urls;
import com.ywb.tuyue.ui.BaseFragment;
import com.ywb.tuyue.utils.DeviceUtils;
import com.ywb.tuyue.utils.FileUtils;
import com.ywb.tuyue.utils.MathUtils;
import com.ywb.tuyue.utils.StringUtils;
import com.ywb.tuyue.utils.SystemUtils;
import com.ywb.tuyue.utils.T;
import com.ywb.tuyue.utils.TimerManager;
import com.ywb.tuyue.utils.UiHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.RequestErrorException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by penghao on 2017/12/18.
 * description： 数据同步
 */

public class DataSynchronizationFrag extends BaseFragment {
    public static final String TAG = "DataSynchronizationFrag";
    @BindView(R.id.data_versionStatus)
    TextView dataVersionStatus;
    @BindView(R.id.data_click_upGrade)
    TextView dataClickUpGrade;
    @BindView(R.id.data_CurrentVersion)
    TextView dataCurrentVersion;
    @BindView(R.id.data_sysTime)
    TextView dataSysTime;
    @BindView(R.id.is_new)
    TextView isNew;
    @BindView(R.id.ll_update)
    LinearLayout update;
    @BindView(R.id.city_sync)
    TextView dataSync;
    @BindView(R.id.error)
    TextView errorInfo;
    private DataSyncInfo.ResultBean resultBean;
    private String hasNew;

    private Queue<DownLoadTask> downLoadTaskQueue = new LinkedList<>();
    private Queue<DownLoadTask> test1905 = new LinkedList<>();

    public static final boolean NO = false;
    public static final boolean YES = true;
    private boolean isCurrentActivity;

    boolean isHaseRealTask = false;
    private Map<Long, Disposable> requestTaskMap = new HashMap<>();

    private List<String> sUrls = new ArrayList<>();
    private List<String> localUrls;

    @Override
    protected int getViewId() {
        return R.layout.frag_data_syn;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        dataClickUpGrade.setText("同步电影数据");
        String lastTimeStr = DataBase.getString(AppConfig.TIME);
        if (!TextUtils.isEmpty(lastTimeStr)) {
            dataSysTime.setText(lastTimeStr + "");
        } else {
            dataSysTime.setText("1970-01-01 00:00:00");
        }
//      checkHasNewDataSync();

        //启动定时任务
        new TimerManager(this);

        dataSync.setOnClickListener(v -> startSyncData(YES));

        preDownLoadManager();

    }

    private void preDownLoadManager() {
        RxPermissions.getInstance(mActivity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(aBoolean -> {
                    if (!aBoolean) {
                        throw new RuntimeException("no permission");
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 判断是否有数据需要同步
     */
    private void checkHasNewDataSync() {
        OkHttpUtils//
                .get()//
                .tag(this)//
                .url(Urls.getDataSyncInfo)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("imei", DeviceUtils.getUniqueId(mActivity))//移动设备IMEI
                .build()//
                .execute(new AppGsonCallback<DataSyncInfo>(new RequestModel(mActivity)) {
                    @Override
                    public void onResponseOK(DataSyncInfo response, int id) {
                        super.onResponseOK(response, id);
                        resultBean = response.getResult();
                        if (resultBean != null) {
                            hasNew = resultBean.getHasNew();
                            if (update == null || isNew == null) {
                                return;
                            }
                            if (TextUtils.equals("1", hasNew)) {//代表有更新
                                update.setVisibility(View.VISIBLE);
                                isNew.setVisibility(View.GONE);
                            } else {
                                update.setVisibility(View.GONE);
                                isNew.setVisibility(View.VISIBLE);
                            }
                            if (resultBean.getLastSyncTime() != null) {
                                long create_at = resultBean.getLastSyncTime().getCreate_at();
                                dataSysTime.setText(T.getDate(create_at));
                            }

                        }

                    }
                });
    }


    @OnClick(R.id.data_click_upGrade)
    public void onViewClicked() {
        startSyncData(YES);
    }

    private Disposable getSyncDataDisposable;
    String countData;

    /**
     * 获取需要同步的数据，并下载
     */
    public void startSyncData(boolean isCurrentActivity) {
        localUrls = MovieDao.getInstance().getMovieListUrls();//获取本地数据库存储的URL
        //for count
        Count lastCount = CountDao.getInstance().getLastCount();
        if (lastCount != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            //时间
            lastCount.setCreateTime(date);
            //设备IMEI
            lastCount.setPadImei(DeviceUtils.getUniqueId(mActivity));
            //开机次数
            lastCount.setOpenPad(DataBase.getInt(AppConfig.openCount));
            CountDao.getInstance().updateCount(lastCount);
        }

        countData = new Gson().toJson(CountDao.getInstance().getLastCount() == null ? "" : CountDao.getInstance().getLastCount());
        Log.d(TAG, "统计: " + countData);

        isHaseRealTask = false;
        this.isCurrentActivity = isCurrentActivity;


        if (this.isCurrentActivity) {
            UiHelper.getInstance().showLoading(mActivity);
        }

        getSyncDataDisposable = Observable.create(subscribe -> {
            //获取电影数据
            Response response = OkHttpUtils//
                    .get()//
                    .tag(this)//
                    .url(Urls.getMoviesForSync)//
                    .addParams(AppConfig.token_key, AppConfig.token_value)//
                    .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                    .addParams("imei", DeviceUtils.getUniqueId(mActivity))//移动设备IMEI
                    .build()//
                    .execute();

            String backData = response.body().string();
            MovieSyncDataBean movieSyncDataBean = GsonSerializator.getInstance().transform(backData, MovieSyncDataBean.class);
            if (AppConfig.isRequestOk(movieSyncDataBean.getCode())) {

                MovieSyncDataBean.ResultBean result = movieSyncDataBean.getResult();
                if (result != null) {
//                    showLoadInfo(result.getTotalSize());
                    MovieSyncDataBean.ResultBean.MovieBannerBean movieBanner = result.getMovieBanner();//映前广告
                    List<MoviesListBean> moviesList = result.getMoviesList();//待下载的电影列表
                    List<MoviesListBean> localList = MovieDao.getInstance().getMovieList();//获取本地数据库
                    List<MovieSyncDataBean.ResultBean.MovieBannerBean> localBanner = BannerDao.getInstance().getAllBanner();
                    //服务器电影id列表
//                    List<Integer> serverMovieIds = new ArrayList<>();

                    if (moviesList != null && !moviesList.isEmpty()) {
                        //将服务器电影数据插入数据库
                        for (MoviesListBean moviesListBean : moviesList) {

                            //for 1905 解码
                            String url = StringUtils.dealStr(moviesListBean.getDownload_url());

                            moviesListBean.setDownload_url(url);
                            moviesListBean.setPoster_url(StringUtils.dealStr(moviesListBean.getPoster_url()));

                            MovieDao.getInstance().updateMovie(moviesListBean);

                            //存储服务器id
                            sUrls.add(moviesListBean.getDownload_url());

                            //存储本次可能需要下载的任务
                            downLoadTaskQueue.offer(new DownLoadTask(moviesListBean.getDownload_url(), FileUtils.getFileName(moviesListBean.getDownload_url()), DownLoadTask.VIDEO, -1));//视屏
                            downLoadTaskQueue.offer(new DownLoadTask(moviesListBean.getPoster_url(), FileUtils.getFileName(moviesListBean.getPoster_url()), DownLoadTask.IMG, -2));//图片
                        }

                    }
                    //添加电影映前广告下载
                    if (movieBanner != null) {
                        String img_url = movieBanner.getImg_url();
                        movieBanner.setImg_url(StringUtils.dealStr(img_url));
                        BannerDao.getInstance().updateBanner(movieBanner);
                        downLoadTaskQueue.offer(new DownLoadTask(movieBanner.getImg_url(), FileUtils.getFileName(img_url), DownLoadTask.IMG, -1));
                    }
                    //无用图片剔除
                    if (localBanner != null && !localBanner.isEmpty()) {
                        for (MovieSyncDataBean.ResultBean.MovieBannerBean movieBannerBean : localBanner) {
                            if (movieBanner != null) {
                                if (!TextUtils.equals(movieBannerBean.getImg_url(), movieBanner.getImg_url())) {
                                    if (BannerDao.getInstance().getObjectFromId(movieBannerBean.getId()) != null) {
                                        MyDownLoadManager.removeTask(movieBannerBean.getImg_url());//删除图片文件
                                    }
                                    if (movieBannerBean.getId() != movieBanner.getId()) {
                                        BannerDao.getInstance().removeOne(movieBannerBean.getId());//删除数据库记录
                                    }
                                }
                            }
                        }

                    }


                    //删除本地历史数据（服务器不存在的）
//                    if (localList != null)
//                        for (MoviesListBean moviesListBean : localList) {
//                            int localMovieId = moviesListBean.getId();
//                            if (!serverMovieIds.isEmpty() && !serverMovieIds.contains(localMovieId)) {//如果不包含
//                                if (MovieDao.getInstance().getObjectFromId(localMovieId) != null) {
//                                    MyDownLoadManager.removeTask(moviesListBean.getDownload_url());//删除电影文件
//                                    MyDownLoadManager.removeTask(moviesListBean.getPoster_url());//删除图片文件
//                                }
//                                MovieDao.getInstance().removeOne(localMovieId);//删除数据库记录
//                            }
//                        }

                }

                subscribe.onNext(Activity.RESULT_OK);
            } else {
                subscribe.onError(new RequestErrorException(movieSyncDataBean.getMessage()));
            }
            subscribe.onComplete();
        }).subscribeOn(Schedulers.io())
                .flatMap(o -> {
                    //获取首页数据
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.homePage)//
                            .addParams(AppConfig.token_key, AppConfig.token_value)//
                            .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                            .build()//
                            .execute();
                    String backData = response.body().string();
                    HomeBean homeBean = GsonSerializator.getInstance().transform(backData, HomeBean.class);
                    if (AppConfig.isRequestOk(homeBean.getCode())) {
                        HomeBean.ResultBean result = homeBean.getResult();
                        //添加IP
                        if (result != null) {
                            List<HomeBean.ResultBean.AdvertBean> currentAdvert = result.getAdvert();
                            if (currentAdvert != null && !currentAdvert.isEmpty()) {
                                for (HomeBean.ResultBean.AdvertBean currentBean : currentAdvert) {
                                    currentBean.setImg_url(StringUtils.dealStr(currentBean.getImg_url()));
                                }
                            }
                            List<HomeBean.ResultBean.HotMovieBean> currentMovieBeans = result.getHotMovie();
                            if (currentMovieBeans != null && !currentMovieBeans.isEmpty()) {
                                for (HomeBean.ResultBean.HotMovieBean currentBean : currentMovieBeans) {
                                    currentBean.setPoster_url(StringUtils.dealStr(currentBean.getPoster_url()));
                                }
                            }
                        }

                        //剔除
                        String historyData = DataBase.getString(AppConfig.HOMEPAGE);
                        if (!TextUtils.isEmpty(historyData)) {
                            HomeBean historyCity = GsonSerializator.getInstance().transform(historyData, HomeBean.class);
                            HomeBean.ResultBean historyCityResult = historyCity.getResult();
                            if (historyCityResult != null) {
                                List<HomeBean.ResultBean.AdvertBean> historyAdvert = historyCityResult.getAdvert();
                                List<HomeBean.ResultBean.HotMovieBean> historyMovie = historyCityResult.getHotMovie();
                                //广告图片剔除
                                if (historyAdvert != null && !historyAdvert.isEmpty()) {
                                    for (HomeBean.ResultBean.AdvertBean historyAdvertBean : historyAdvert) {
                                        boolean isExistService = false;
                                        List<HomeBean.ResultBean.AdvertBean> advert = result.getAdvert();
                                        if (advert != null && !advert.isEmpty()) {
                                            for (HomeBean.ResultBean.AdvertBean currentBean : advert) {
                                                if (historyAdvertBean.getImg_url().equals(currentBean.getImg_url())) {
                                                    isExistService = true;
                                                    break;
                                                }
                                            }
                                            if (!isExistService) {
                                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyAdvertBean.getImg_url()));
                                            }
                                        }
                                    }
                                }
                                //热门电影图片剔除
                                if (historyMovie != null && !historyMovie.isEmpty()) {
                                    for (HomeBean.ResultBean.HotMovieBean historyHotMovieBean : historyMovie) {
                                        boolean isExistService = false;
                                        List<HomeBean.ResultBean.HotMovieBean> movieBeans = result.getHotMovie();
                                        if (movieBeans != null && !movieBeans.isEmpty()) {
                                            for (HomeBean.ResultBean.HotMovieBean currentBean : movieBeans) {
                                                if (historyHotMovieBean.getPoster_url().equals(currentBean.getPoster_url())) {
                                                    isExistService = true;
                                                    break;
                                                }
                                            }
                                            if (!isExistService) {
                                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyHotMovieBean.getPoster_url()));
                                            }
                                        }
                                    }
                                }
                            }


                        }
                        if (result != null) {
                            List<HomeBean.ResultBean.AdvertBean> temp = result.getAdvert();
                            List<HomeBean.ResultBean.HotMovieBean> hotMovie = result.getHotMovie();

                            //广告图片
                            if (null != temp && !temp.isEmpty()) {
                                for (int i = 0; i < temp.size(); i++) {
                                    HomeBean.ResultBean.AdvertBean advertBean = temp.get(i);
                                    downLoadTaskQueue.offer(new DownLoadTask(advertBean.getImg_url(), FileUtils.getFileName(advertBean.getImg_url()), DownLoadTask.IMG, -1));//图片
                                }
                            }
                            //热门电影图片
                            if (null != hotMovie && !hotMovie.isEmpty()) {
                                HomeBean.ResultBean.HotMovieBean hotMovieBean = hotMovie.get(0);
                                downLoadTaskQueue.offer(new DownLoadTask(hotMovieBean.getPoster_url(), FileUtils.getFileName(hotMovieBean.getPoster_url()), DownLoadTask.IMG, -1));//图片
                            }
                            String newData = new Gson().toJson(homeBean);

                            DataBase.saveString(AppConfig.HOMEPAGE, newData);

                        } else {
                            throw new RequestErrorException(homeBean.getMessage());
                        }
                    }
                    return Observable.just(1);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取城市列表数据
                    Response response = OkHttpUtils
                            .get()//
                            .tag(this)//
                            .url(Urls.getCityList)
                            .addParams(AppConfig.token_key, AppConfig.token_value)//
                            .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                            .addParams(AppConfig.page, "1")//
                            .addParams(AppConfig.pageSize, "100")//
                            .build()//
                            .execute();

                    String backData = response.body().string();
                    City city = GsonSerializator.getInstance().transform(backData, City.class);


                    if (AppConfig.isRequestOk(city.getCode())) {
                        //添加IP
                        List<City.ResultBean> result = city.getResult();
                        if (result != null && !result.isEmpty()) {
                            for (City.ResultBean currentBean : result) {
                                currentBean.setImg_url(StringUtils.dealStr(currentBean.getImg_url()));
                            }
                        }
                        //剔除
                        String historyData = DataBase.getString(AppConfig.CITY_DATA);
                        if (!TextUtils.isEmpty(historyData)) {
                            City historyCity = GsonSerializator.getInstance().transform(historyData, City.class);
                            if (historyCity.getResult() != null && !historyCity.getResult().isEmpty()) {
                                for (City.ResultBean historyBean : historyCity.getResult()) {
                                    boolean isExitService = false;
                                    if (result != null && !result.isEmpty()) {
                                        for (City.ResultBean currentBean : result) {
                                            if (historyBean.getImg_url().equals(currentBean.getImg_url())) {
                                                isExitService = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isExitService) {
                                        MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyBean.getImg_url()));
                                    }

                                }
                            }

                        }


                        if (result != null && !result.isEmpty()) {
                            for (int i = 0; i < result.size(); i++) {
                                downLoadTaskQueue.offer(new DownLoadTask(result.get(i).getImg_url(), FileUtils.getFileName(result.get(i).getImg_url()), DownLoadTask.IMG, -1));//图片
                            }
                        }
                        String newData = new Gson().toJson(city);
                        DataBase.saveString(AppConfig.CITY_DATA, newData);
                    } else {
                        throw new RequestErrorException(city.getMessage());
                    }
                    return Observable.just(2);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取解锁页背景图
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getStdbyAdvert)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    HomeAdvertBean homeAdvertBean = GsonSerializator.getInstance().transform(backData, HomeAdvertBean.class);

                    if (AppConfig.isRequestOk(homeAdvertBean.getCode())) {
                        List<HomeAdvertBean.ResultBean> result = homeAdvertBean.getResult();
                        if (result != null && !result.isEmpty()) {
                            String bgUrl = result.get(0).getImg_url();
                            result.get(0).setImg_url(StringUtils.dealStr(bgUrl));
                        }
                        //剔除图片
                        String historyData = DataBase.getString(AppConfig.GETSTDBYADVERT);
                        if (!TextUtils.isEmpty(historyData)) {
                            HomeAdvertBean historyBean = GsonSerializator.getInstance().transform(historyData, HomeAdvertBean.class);
                            List<HomeAdvertBean.ResultBean> historyBeanResult = historyBean.getResult();
                            if (historyBeanResult != null && !historyBeanResult.isEmpty()) {
                                if (result != null && !result.isEmpty()) {
                                    String bgUrl = result.get(0).getImg_url();
                                    String historyUrl = historyBeanResult.get(0).getImg_url();
                                    if (!TextUtils.equals(historyUrl, bgUrl)) {
                                        MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyUrl));
                                    }
                                }

                            }
                        }

                        if (result != null && !result.isEmpty()) {
                            String bgUrl = result.get(0).getImg_url();
                            if (!TextUtils.isEmpty(bgUrl)) {
                                //添加下载信息
                                downLoadTaskQueue.offer(new DownLoadTask(bgUrl, FileUtils.getFileName(bgUrl), DownLoadTask.IMG, -1));//图片
                            }
                        }
                        String newData = new Gson().toJson(homeAdvertBean);
                        DataBase.saveString(AppConfig.GETSTDBYADVERT, newData);

                    } else {
                        throw new RequestErrorException(homeAdvertBean.getMessage());
                    }

                    return Observable.just(3);
                }).subscribeOn(Schedulers.io())
                .flatMap(start -> {
                    //获取文章列表
                    Response response = OkHttpUtils
                            .get()//
                            .tag(this)//
                            .url(Urls.getArticleList)
                            .addParams(AppConfig.token_key, AppConfig.token_value)//
                            .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                            .addParams(AppConfig.page, "1")//
                            .addParams(AppConfig.pageSize, "100")//
                            .addParams("typeId", "2")//假数据
                            .build()//
                            .execute();

                    String backData = response.body().string();
                    ChengTie chengTie = GsonSerializator.getInstance().transform(backData, ChengTie.class);

                    List<String> videoHisList = new ArrayList<>();
                    List<String> imageHisList = new ArrayList<>();
                    List<String> videoCurrentList = new ArrayList<>();
                    List<String> imageCurrentList = new ArrayList<>();

                    if (AppConfig.isRequestOk(chengTie.getCode())) {
                        List<ChengTie.ResultBean> result = chengTie.getResult();


                        //遍历服务器数据，取服务器数据的图片和视频链接
                        if (result != null && !result.isEmpty()) {
                            for (int i = 0; i < result.size(); i++) {
                                List<ChengTie.ResultBean.ArticleListBean> currentBeanList = result.get(i).getArticle_list();
                                if (currentBeanList != null && !currentBeanList.isEmpty()) {

                                    for (ChengTie.ResultBean.ArticleListBean articleListBean : currentBeanList) {
                                        String currentImageUrl = articleListBean.getImg_url();
                                        articleListBean.setImg_url(StringUtils.dealStr(currentImageUrl));
                                        if (TextUtils.equals("2", articleListBean.getContent_type())) {
                                            String currentVideoUrl = articleListBean.getContent_img();
                                            articleListBean.setContent_img(StringUtils.dealStr(currentVideoUrl));
                                            videoCurrentList.add(articleListBean.getContent_img());
                                        }
                                        imageCurrentList.add(articleListBean.getImg_url());
                                    }
                                }

                            }
                        }
                        //剔除
                        String history = DataBase.getString(AppConfig.GETARTICLELIST);
                        if (!TextUtils.isEmpty(history)) {
                            ChengTie historyChengTie = GsonSerializator.getInstance().transform(history, ChengTie.class);
                            List<ChengTie.ResultBean> historyChengTieResult = historyChengTie.getResult();

                            //取本地数据库的图片和视频链接
                            if (historyChengTieResult != null && !historyChengTieResult.isEmpty()) {
                                for (int j = 0; j < historyChengTieResult.size(); j++) {
                                    List<ChengTie.ResultBean.ArticleListBean> historyBeanList = historyChengTieResult.get(j).getArticle_list();
                                    if (historyBeanList != null && !historyBeanList.isEmpty()) {
                                        for (ChengTie.ResultBean.ArticleListBean historyUrlBean : historyBeanList) {
                                            boolean isVideo = TextUtils.equals("2", historyUrlBean.getContent_type());
                                            String historyImageUrl = historyUrlBean.getImg_url();//图文封面
                                            if (isVideo) {
                                                String historyUrl = historyUrlBean.getContent_img();//电影链接
                                                videoHisList.add(historyUrl);
                                            }
                                            imageHisList.add(historyImageUrl);
                                        }
                                    }

                                }

                            }

                            //比较，视频地址去重
                            for (String historyVideoUrl : videoHisList) {
                                if (!videoCurrentList.isEmpty() && !videoCurrentList.contains(historyVideoUrl)) {
                                    MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyVideoUrl));
                                }
                            }
                            //比较，图片地址去重
                            for (String historyImageUrl : imageHisList) {
                                if (!imageCurrentList.isEmpty() && !imageCurrentList.contains(historyImageUrl)) {
                                    MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyImageUrl));
                                }
                            }
                        }

                        //添加下载信息
                        if (result != null && !result.isEmpty()) {
                            for (int i = 0; i < result.size(); i++) {
                                List<ChengTie.ResultBean.ArticleListBean> article_list = result.get(i).getArticle_list();
                                for (int i1 = 0; i1 < article_list.size(); i1++) {
                                    boolean isVideo = TextUtils.equals("2", article_list.get(i1).getContent_type());
                                    String downUrl = article_list.get(i1).getContent_img();
                                    String imageUrl = article_list.get(i1).getImg_url();

                                    if (isVideo) {
                                        downLoadTaskQueue.offer(new DownLoadTask(downUrl, FileUtils.getFileName(downUrl), DownLoadTask.VIDEO, -1));//视屏
                                    } else {
//                                    if (!TextUtils.isEmpty(downUrl)) {
//                                        Log.d(TAG, "downUrl: " + downUrl);
//                                        String[] split = downUrl.split(",");
//                                        if (split != null && split.length > 1) {
//                                            downLoadTaskQueue.offer(new DownLoadTask(split[0], article_list.get(i1).getTitle() + 0 + i1 + ".jpg", DownLoadTask.IMG, -1));//图片
//                                            downLoadTaskQueue.offer(new DownLoadTask(split[1], article_list.get(i1).getTitle() + 1 + i1 + ".jpg", DownLoadTask.IMG, -1));//图片
//                                        }
//                                    }
                                    }
                                    downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                }
                            }
                        }
                        String newData = new Gson().toJson(chengTie);

                        DataBase.saveString(AppConfig.GETARTICLELIST, newData);

                    } else {
                        throw new RequestErrorException(chengTie.getMessage());
                    }

                    return Observable.just(4);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取电影栏目
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getMoviesConfigTypeList)//
                            .addParams(AppConfig.token_key, AppConfig.token_value)//
                            .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                            .addParams(AppConfig.page, "1")//
                            .addParams(AppConfig.pageSize, "100")//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    MovieMenuBean movieMenuBean = GsonSerializator.getInstance().transform(backData, MovieMenuBean.class);

                    if (AppConfig.isRequestOk(movieMenuBean.getCode())) {
                        DataBase.saveString(AppConfig.GETMOVIESCONFIGTYPELIST, backData);
                    } else {
                        throw new RequestErrorException(movieMenuBean.getMessage());
                    }

                    return Observable.just(5);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取点餐中的图片
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.listAllFood)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    List<String> imageHisList = new ArrayList<>();
                    List<String> imageCurrentList = new ArrayList<>();

                    FoodMenuBean foodMenuBean = GsonSerializator.getInstance().transform(backData, FoodMenuBean.class);
                    if (AppConfig.isRequestOk(foodMenuBean.getCode())) {
                        List<FoodMenuBean.ResultBean> currentDataBean = foodMenuBean.getResult();
                        String historyData = DataBase.getString(AppConfig.LISTALLFOOD);

                        if (currentDataBean != null && !currentDataBean.isEmpty()) {
                            for (FoodMenuBean.ResultBean currentBean : currentDataBean) {
                                List<FoodMenuBean.ResultBean.FoodListBean> currentUrlsBean = currentBean.getFoodList();
                                if (currentUrlsBean != null && !currentUrlsBean.isEmpty()) {
                                    for (FoodMenuBean.ResultBean.FoodListBean Urls : currentUrlsBean) {
                                        String url = Urls.getUrl();
                                        Urls.setUrl(StringUtils.dealStr(url));
                                        imageCurrentList.add(Urls.getUrl());
                                    }
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(historyData)) {
                            FoodMenuBean historyDataBean = GsonSerializator.getInstance().transform(historyData, FoodMenuBean.class);
                            List<FoodMenuBean.ResultBean> historyDateBean = historyDataBean.getResult();
                            if (historyDateBean != null && !historyDateBean.isEmpty()) {
                                for (FoodMenuBean.ResultBean historyBean : historyDateBean) {
                                    List<FoodMenuBean.ResultBean.FoodListBean> histUrlsBean = historyBean.getFoodList();
                                    if (histUrlsBean != null && !histUrlsBean.isEmpty()) {
                                        for (FoodMenuBean.ResultBean.FoodListBean historyUrls : histUrlsBean) {
                                            String url = historyUrls.getUrl();
                                            imageHisList.add(url);
                                        }
                                    }
                                }
                            }
                        }


                        //比较，图片地址去重
                        for (String historyImageUrl : imageHisList) {
                            if (!imageCurrentList.isEmpty() && !imageCurrentList.contains(historyImageUrl)) {
                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyImageUrl));
                            }
                        }

                        //添加下载信息
                        if (currentDataBean != null && !currentDataBean.isEmpty()) {
                            for (int i = 0; i < currentDataBean.size(); i++) {
                                List<FoodMenuBean.ResultBean.FoodListBean> foodList = currentDataBean.get(i).getFoodList();
                                for (int i1 = 0; i1 < foodList.size(); i1++) {
                                    String imageUrl = foodList.get(i1).getUrl();
                                    downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                }
                            }
                        }
                        String newData = new Gson().toJson(foodMenuBean);
                        DataBase.saveString(AppConfig.LISTALLFOOD, newData);
                    } else {
                        throw new RequestErrorException(foodMenuBean.getMessage());
                    }

                    return Observable.just(6);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取电玩城中的图片和zip下载链接
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getGames)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    List<String> imageHisList = new ArrayList<>();
                    List<String> imageCurrentList = new ArrayList<>();


                    GameBean gameBean = GsonSerializator.getInstance().transform(backData, GameBean.class);
                    if (AppConfig.isRequestOk(gameBean.getCode())) {
                        GameBean.ResultBean currentDataBean = gameBean.getResult();
                        if (currentDataBean != null) {
                            List<GameBean.ResultBean.AdsBean> currentAds = currentDataBean.getAds();
                            List<GameBean.ResultBean.GamesBean> currentGames = currentDataBean.getGames();
                            if (currentAds != null && !currentAds.isEmpty()) {
                                for (GameBean.ResultBean.AdsBean currentAd : currentAds) {
                                    String currentAdImg = currentAd.getImg();
                                    currentAd.setImg(StringUtils.dealStr(currentAdImg));
                                    //添加banner图链接
                                    imageCurrentList.add(currentAd.getImg());
                                }
                            }
                            if (currentGames != null && !currentGames.isEmpty()) {
                                for (GameBean.ResultBean.GamesBean gamesBean : currentGames) {
                                    String gamesImg = gamesBean.getImg();
                                    gamesBean.setImg(StringUtils.dealStr(gamesImg));

                                    String zip = gamesBean.getGame_zip();
                                    gamesBean.setGame_zip(StringUtils.dealStr(zip));

                                    //添加游戏封面链接和ZIP
                                    imageCurrentList.add(gamesBean.getImg());
                                    imageCurrentList.add(gamesBean.getGame_zip());
                                }
                            }
                        }

                        String historyData = DataBase.getString(AppConfig.GAME_DATA);

                        if (!TextUtils.isEmpty(historyData)) {
                            GameBean historyDataBean = GsonSerializator.getInstance().transform(historyData, GameBean.class);
                            GameBean.ResultBean historyDateBean = historyDataBean.getResult();

                            if (historyDateBean != null) {
                                List<GameBean.ResultBean.AdsBean> historyAds = historyDateBean.getAds();
                                List<GameBean.ResultBean.GamesBean> historyGames = historyDateBean.getGames();

                                if (historyAds != null && !historyAds.isEmpty()) {
                                    for (GameBean.ResultBean.AdsBean historyAd : historyAds) {
                                        imageHisList.add(historyAd.getImg());
                                    }
                                }
                                if (historyGames != null && !historyGames.isEmpty()) {
                                    for (GameBean.ResultBean.GamesBean historyGame : historyGames) {
                                        imageHisList.add(historyGame.getImg());
                                    }
                                }
                            }
                        }

                        //比较，图片地址去重
                        for (String historyImageUrl : imageHisList) {
                            if (!imageCurrentList.isEmpty() && !imageCurrentList.contains(historyImageUrl)) {
                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyImageUrl));
                            }
                        }

                        //添加下载信息
                        if (currentDataBean != null) {
                            List<GameBean.ResultBean.AdsBean> ads = currentDataBean.getAds();
                            List<GameBean.ResultBean.GamesBean> gamesBeans = currentDataBean.getGames();
                            if (ads != null && !ads.isEmpty()) {
                                for (GameBean.ResultBean.AdsBean ad : ads) {
                                    String imageUrl = ad.getImg();
                                    if (!TextUtils.isEmpty(imageUrl)) {
                                        downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                    }
                                }
                            }
                            if (gamesBeans != null && !gamesBeans.isEmpty()) {
                                for (GameBean.ResultBean.GamesBean game : gamesBeans) {
                                    String imageUrl = game.getImg();
                                    String game_zip = game.getGame_zip();
                                    if (!TextUtils.isEmpty(imageUrl)) {
                                        downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                    }
                                    if (!TextUtils.isEmpty(game_zip)) {
                                        downLoadTaskQueue.offer(new DownLoadTask(game_zip, FileUtils.getFileName(game_zip), DownLoadTask.ZIP, 100));//图片
                                    }
                                }
                            }

                        }
                        String newData = new Gson().toJson(gameBean);
                        //存储
                        DataBase.saveString(AppConfig.GAME_DATA, newData);
                    } else {
                        throw new RequestErrorException(gameBean.getMessage());
                    }

                    return Observable.just(7);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取书吧中的图片和书的下载链接
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getBooks)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    List<String> imageHisList = new ArrayList<>();
                    List<String> imageCurrentList = new ArrayList<>();

                    BookBean bookBean = GsonSerializator.getInstance().transform(backData, BookBean.class);
                    if (AppConfig.isRequestOk(bookBean.getCode())) {
                        BookBean.ResultBean currentDataBean = bookBean.getResult();
                        if (currentDataBean != null) {
                            List<BookBean.ResultBean.AdsBean> currentAds = currentDataBean.getAds();
                            List<BookBean.ResultBean.BooksBeanX> currentBooks = currentDataBean.getBooks();
                            if (currentAds != null && !currentAds.isEmpty()) {
                                for (BookBean.ResultBean.AdsBean currentAd : currentAds) {
                                    String currentAdImg = currentAd.getImg();
                                    currentAd.setImg(StringUtils.dealStr(currentAdImg));
                                    //添加banner图链接
                                    imageCurrentList.add(currentAd.getImg());
                                }
                            }
                            if (currentBooks != null && !currentBooks.isEmpty()) {
                                for (BookBean.ResultBean.BooksBeanX booksBeanX : currentBooks) {
                                    List<BookBean.ResultBean.BooksBeanX.BooksBean> books = booksBeanX.getBooks();
                                    if (books != null && !books.isEmpty()) {
                                        for (BookBean.ResultBean.BooksBeanX.BooksBean book : books) {
                                            String img = book.getImg();//书本封面下载链接
                                            String book_url = book.getBook_url();//书本下载链接
                                            book.setImg(StringUtils.dealStr(img));
                                            book.setBook_url(StringUtils.dealStr(book_url));
                                            //添加书本封面链接
                                            imageCurrentList.add(book.getImg());
                                            //添加书本下载链接
                                            imageCurrentList.add(book.getBook_url());
                                        }
                                    }

                                }
                            }
                        }

                        String historyData = DataBase.getString(AppConfig.BOOK_DATA);

                        if (!TextUtils.isEmpty(historyData)) {
                            BookBean historyDataBean = GsonSerializator.getInstance().transform(historyData, BookBean.class);
                            BookBean.ResultBean historyDateBean = historyDataBean.getResult();

                            if (historyDateBean != null) {
                                List<BookBean.ResultBean.AdsBean> historyAds = historyDateBean.getAds();
                                List<BookBean.ResultBean.BooksBeanX> historyBooks = historyDateBean.getBooks();

                                if (historyAds != null && !historyAds.isEmpty()) {
                                    for (BookBean.ResultBean.AdsBean historyAd : historyAds) {
                                        imageHisList.add(historyAd.getImg());
                                    }
                                }
                                if (historyBooks != null && !historyBooks.isEmpty()) {
                                    for (BookBean.ResultBean.BooksBeanX historyBook : historyBooks) {
                                        List<BookBean.ResultBean.BooksBeanX.BooksBean> books = historyBook.getBooks();
                                        if (books != null && !books.isEmpty()) {
                                            for (BookBean.ResultBean.BooksBeanX.BooksBean book : books) {
                                                imageHisList.add(book.getImg());
                                                imageHisList.add(book.getBook_url());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //比较，图片地址去重
                        for (String historyImageUrl : imageHisList) {
                            if (!imageCurrentList.isEmpty() && !imageCurrentList.contains(historyImageUrl)) {
                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyImageUrl));
                            }
                        }

                        //添加下载信息
                        if (currentDataBean != null) {
                            List<BookBean.ResultBean.AdsBean> ads = currentDataBean.getAds();
                            List<BookBean.ResultBean.BooksBeanX> bookBeans = currentDataBean.getBooks();
                            if (ads != null && !ads.isEmpty()) {
                                for (BookBean.ResultBean.AdsBean ad : ads) {
                                    String imageUrl = ad.getImg();
                                    if (!TextUtils.isEmpty(imageUrl)) {
                                        downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                    }
                                }
                            }
                            if (bookBeans != null && !bookBeans.isEmpty()) {
                                for (BookBean.ResultBean.BooksBeanX booksBeanX : bookBeans) {
                                    List<BookBean.ResultBean.BooksBeanX.BooksBean> books = booksBeanX.getBooks();
                                    if (books != null && !books.isEmpty()) {
                                        for (BookBean.ResultBean.BooksBeanX.BooksBean book : books) {
                                            String imageUrl = book.getImg();
                                            String book_url = book.getBook_url();
                                            if (!TextUtils.isEmpty(imageUrl)) {
                                                downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                            }
                                            if (!TextUtils.isEmpty(book_url)) {
                                                downLoadTaskQueue.offer(new DownLoadTask(book_url, "bookList/" + FileUtils.getFileName(book_url), DownLoadTask.UNKNOW, 100));//图片
                                            }
                                        }
                                    }

                                }
                            }

                        }
                        String newData = new Gson().toJson(bookBean);
                        //存储
                        DataBase.saveString(AppConfig.BOOK_DATA, newData);
                    } else {
                        throw new RequestErrorException(bookBean.getMessage());
                    }
                    return Observable.just(8);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取音乐中的图片和歌曲的下载链接
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getMusic)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    List<String> imageHisList = new ArrayList<>();
                    List<String> imageCurrentList = new ArrayList<>();

                    MusicBean musicBean = GsonSerializator.getInstance().transform(backData, MusicBean.class);
                    if (AppConfig.isRequestOk(musicBean.getCode())) {
                        List<MusicBean.ResultBean> result = musicBean.getResult();
                        if (result != null && !result.isEmpty()) {
                            for (MusicBean.ResultBean bean : result) {
                                String img = bean.getImg();
                                String music = bean.getMusic();
                                bean.setImg(StringUtils.dealStr(img));
                                bean.setMusic(StringUtils.dealStr(music));
                                //添加音乐地址和封面
                                imageCurrentList.add(bean.getImg());
                                imageCurrentList.add(bean.getMusic());
                            }
                        }

                        String historyData = DataBase.getString(AppConfig.MUSIC_DATA);

                        if (!TextUtils.isEmpty(historyData)) {
                            MusicBean historyDataBean = GsonSerializator.getInstance().transform(historyData, MusicBean.class);
                            List<MusicBean.ResultBean> historyDateBean = historyDataBean.getResult();

                            if (historyDateBean != null && !historyDateBean.isEmpty()) {

                                for (MusicBean.ResultBean history : historyDateBean) {
                                    imageHisList.add(history.getImg());
                                    imageHisList.add(history.getMusic());
                                }
                            }
                        }

                        //比较，图片地址去重
                        for (String historyImageUrl : imageHisList) {
                            if (!imageCurrentList.isEmpty() && !imageCurrentList.contains(historyImageUrl)) {
                                MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(historyImageUrl));
                            }
                        }

                        //添加下载信息
                        if (result != null && !result.isEmpty()) {
                            for (MusicBean.ResultBean currentBean : result) {
                                String imageUrl = currentBean.getImg();
                                String music = currentBean.getMusic();
                                if (!TextUtils.isEmpty(imageUrl)) {
                                    downLoadTaskQueue.offer(new DownLoadTask(imageUrl, FileUtils.getFileName(imageUrl), DownLoadTask.IMG, -1));//图片
                                }
                                if (!TextUtils.isEmpty(music)) {
                                    downLoadTaskQueue.offer(new DownLoadTask(music, "musicList/" + FileUtils.getFileName(music), DownLoadTask.UNKNOW, -1));//音乐
                                }
                            }

                        }
                        String newData = new Gson().toJson(musicBean);
                        //存储
                        DataBase.saveString(AppConfig.MUSIC_DATA, newData);
                    } else {
                        throw new RequestErrorException(musicBean.getMessage());
                    }
                    return Observable.just(9);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取富文本中的图片
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.getResourceFile)//
                            .addParams(AppConfig.token_key, AppConfig.token_value)//
                            .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    RichTextBean richTextBean = GsonSerializator.getInstance().transform(backData, RichTextBean.class);
                    if (AppConfig.isRequestOk(richTextBean.getCode())) {
                        String historyData = DataBase.getString(AppConfig.GETRESOURCEFILE);
                        if (richTextBean.getResult() != null) {//上次同步时间
                            String currentData = richTextBean.getResult().getAllFileUrl();
                            DataBase.saveString(AppConfig.TIME, richTextBean.getResult().getTime());
                            if (!TextUtils.isEmpty(historyData)) {
                                RichTextBean historyBean = GsonSerializator.getInstance().transform(historyData, RichTextBean.class);
                                if (historyBean.getResult() != null) {
                                    String historyUrls = historyBean.getResult().getAllFileUrl();
                                    if (!TextUtils.isEmpty(historyUrls)) {
                                        String[] historyUrl = historyUrls.split(",");
                                        if (historyUrl != null && historyUrl.length > 0) {
                                            for (String url : historyUrl) {
                                                boolean isExitService = false;
                                                if (!TextUtils.isEmpty(currentData)) {
                                                    String[] currentUrls = currentData.split(",");
                                                    if (currentUrls != null && currentUrls.length > 0) {
                                                        for (String currentUrl : currentUrls) {
                                                            if (TextUtils.equals(StringUtils.dealStr(url), StringUtils.dealStr(currentUrl))) {
                                                                isExitService = true;
                                                            }
                                                        }
                                                    }

                                                }

                                                if (!isExitService) {
                                                    MyDownLoadManager.removeTask(MyDownLoadManager.getDownLoadId(StringUtils.dealStr(url)));
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        String newData = new Gson().toJson(richTextBean);
                        DataBase.saveString(AppConfig.GETRESOURCEFILE, newData);
                    } else {
                        throw new RequestErrorException(richTextBean.getMessage());
                    }
                    if (richTextBean.getResult() != null) {
                        String resUrls = richTextBean.getResult().getAllFileUrl();
                        if (!TextUtils.isEmpty(resUrls)) {
                            String[] urls = resUrls.split(",");
                            if (urls != null && urls.length > 0) {
                                for (int i = 0; i < urls.length; i++) {
                                    downLoadTaskQueue.offer(new DownLoadTask(StringUtils.dealStr(urls[i]), FileUtils.getFileName(urls[i]), DownLoadTask.IMG, -1));//图片
                                }
                            }
                        }
                    }

                    return Observable.just(10);
                }).subscribeOn(Schedulers.io()).flatMap(start -> {
                    //获取1905片源
                    Response response = OkHttpUtils//
                            .get()//
                            .tag(this)//
                            .url(Urls.get1905Url)//
                            .build()//
                            .execute();

                    String backData = response.body().string();

                    Log.d(TAG, "startSyncData: " + backData);

                    Videos1905Bean currentBean = GsonSerializator.getInstance().transform(backData, Videos1905Bean.class);
                    if (currentBean.getRes() == 0) {//代表请求成功
                        Videos1905Bean.DataBeanX data = currentBean.getData();

                        if (data != null) {
                            Videos1905Bean.DataBeanX.DataBean data1 = data.getData();
                            if (data1 != null) {
                                List<Videos1905Bean.DataBeanX.DataBean.VideosBean> videos = data1.getVideos();
                                if (videos != null && !videos.isEmpty()) {

                                    for (Videos1905Bean.DataBeanX.DataBean.VideosBean video : videos) {

                                        String file_name = video.getFile_name();//片名
                                        String save_name = video.getSave_name();//下载地址
                                        String direct = video.getDirect();//导演
                                        String starring = video.getStarring();//主演
                                        String face_pic = video.getFace_pic();//封面
                                        String drama_cn = video.getDrama_cn();//介绍
                                        String id = video.getId();//id

                                        sUrls.add(save_name);

                                        MoviesListBean moviesListBean = new MoviesListBean();
                                        moviesListBean.setName(file_name);
                                        moviesListBean.setDownload_url(save_name);
                                        moviesListBean.setActor(starring);
                                        moviesListBean.setDirector(direct);
                                        moviesListBean.setPoster_url(face_pic);
                                        moviesListBean.setDetail(drama_cn);
                                        try {
                                            moviesListBean.setId(Integer.parseInt(id));
                                        } catch (NumberFormatException e) {
                                        }
                                        moviesListBean.setType_id(AppConfig.TYPE_ID_1905);

                                        MovieDao.getInstance().updateMovie(moviesListBean);
                                        //存储本次可能需要下载的任务
                                        downLoadTaskQueue.offer(new DownLoadTask(moviesListBean.getPoster_url(), FileUtils.getFileName(moviesListBean.getPoster_url()), DownLoadTask.IMG, -2));//图片

                                        File file = new File(AppConfig.DEFAULT_SAVE_PATH + moviesListBean.getName() + ".ts");
                                        if (!file.exists()) {
                                            test1905.offer(new DownLoadTask(StringUtils.dealStr(moviesListBean.getDownload_url()), moviesListBean.getName(), DownLoadTask.VIDEO, -1));
                                        } else {
                                            Log.i("##1905下载##", "已经存在:" + moviesListBean.getName() + ".ts");
                                        }
                                    }

                                }
                                //删除本地历史数据（服务器不存在的）

                                if (null != localUrls && !localUrls.isEmpty()) {
                                    for (String localUrl : localUrls) {

                                        if (null != sUrls && !sUrls.isEmpty() && !sUrls.contains(localUrl)) {
                                            MoviesListBean objectFromUrl = MovieDao.getInstance().getObjectFromUrl(localUrl);
                                            if (objectFromUrl != null) {
                                                MyDownLoadManager.removeTask(localUrl);//删除电影文件
                                                MyDownLoadManager.removeTask(objectFromUrl.getPoster_url());//删除图片文件
                                                MovieDao.getInstance().removeOne(localUrl);//删除数据库记录
                                            }
                                        }
                                    }
                                    //清除
                                    if (null != sUrls) {

                                        sUrls.clear();
                                    }
                                }

                            }
                        }
                    } else {
                        throw new RequestErrorException(currentBean.getData().getStatus_message());
                    }

                    return Observable.just(11);
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> {
                    Log.d("OkHttp: ", "请求成功!");
                    executeTask();
                }, throwable -> {
                    if (throwable instanceof RequestErrorException) {

                        if (this.isCurrentActivity)
                            Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (this.isCurrentActivity) {
                            Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            errorInfo.setText(throwable.getMessage());
                        }
                    }
                    Log.d("OkHttp: ", "请求失败!" + throwable.getMessage());
                    getSyncDataDisposable.dispose();
                    if (this.isCurrentActivity)
                        UiHelper.getInstance().dismissLoading();
                }, () -> {
                    getSyncDataDisposable.dispose();
                    if (this.isCurrentActivity)
                        UiHelper.getInstance().dismissLoading();
                });
    }

    /**
     * 显示下载信息
     *
     * @param totalSize 本次下载总大小
     */

    private void showLoadInfo(String totalSize) {
        String sdCardSize = SystemUtils.getSDAvailableSize(mActivity);
        double size = 0;
        try {
            size = Double.parseDouble(totalSize);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "totalSize: " + totalSize + "M,sdCardSize:" + sdCardSize);
        Toast.makeText(mActivity, "本次下载大小：" + (MathUtils.fromatFloat2(size / 1024.0f)) + "GB,总容量：" + sdCardSize, Toast.LENGTH_LONG).show();
    }

    /**
     * 从任务队列中取出数据执行下载操作
     */
    private void executeTask() {
        DownLoadTask downLoadTask = downLoadTaskQueue.poll();

        if (downLoadTask != null) {

            startDown(downLoadTask);

        } else {//正常下载完成，开始下载1905

            DownLoadTask downLoadTask1905 = test1905.poll();
            if (downLoadTask1905 != null) {
                if (isCurrentActivity) {
                    //下载对话框
                    showUpGradeDialog();
                }
                if (progressBar != null) {
                    progressBar.setProgress(0);
                }
                if (loadingState != null) {
                    loadingState.setText("正在下载");
                }
                new Runnable() {
                    @Override
                    public void run() {

                        OkGo.<File>get(downLoadTask1905.getDownLoadUrl()).tag(this).execute(new FileCallback(AppConfig.DEFAULT_SAVE_PATH, downLoadTask1905.getFileName() + ".ts") {
                            @Override
                            public void onStart(Request<File, ? extends Request> request) {
                                super.onStart(request);
                                Log.i("##1905下载##", "开始下载:" + request.getUrl());
                                if (movieName != null) {
                                    movieName.setText(downLoadTask1905.getFileName());
                                }
                            }

                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                                Log.i("##1905下载##", "下载成功" + response.message());
                                Log.i("##1905下载##", "下载成功" + response.code());
                                if (response.body() != null) {

                                    Log.i("##1905下载##", "下载成功" + response.body().getPath());
                                    Log.i("##1905下载##", "下载成功" + response.body().getName());
                                    Log.i("##1905下载##", "下载成功" + response.body().getAbsolutePath());
                                }

                                if (loadingState != null) {
                                    loadingState.setText("下载成功");
                                }

                                executeTask();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                Log.i("##1905下载##", "下载完成");
//                                if (loadingState != null) {
//                                    loadingState.setText("下载完成");
//                                }
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                super.downloadProgress(progress);
                                Log.i("##1905下载##", progress.fraction * 100 + "%");

                                if (progressBar != null) {
                                    progressBar.setProgress((int) progress.fraction * 100);
                                }

                                if (loadingPercent != null) {
                                    loadingPercent.setText((int) progress.fraction * 100 + "%");//设置进度条进度
                                }

                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<File> response) {
                                super.onError(response);
                                Log.i("##1905下载##", "下载失败" + response.message());
                                Log.i("##1905下载##", "下载失败" + response.code());
                                if (response.body() != null) {
                                    Log.i("##1905下载##", "下载失败" + response.body().getName());
                                    Log.i("##1905下载##", "下载失败" + response.body().getPath());
                                }
                                if (loadingState != null) {
                                    loadingState.setText("下载失败");
                                }
                            }
                        });
                    }
                }.run();
            } else {
                if (isCurrentActivity) {
                    if (isHaseRealTask) {
                        if (loadingState != null) {
                            loadingState.setText("下载完成");
                        }
                    } else {
                        Toast.makeText(mActivity, "恭喜您,已是最新数据了!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    public String myUrl = "http://192.168.1.6:8088/data/micro_ticket/dst/gao_tie/480p_2/319d48a27491d948ef288ce7664a03c0.mp4";
    public String testUrl = "http://139.196.86.51:8088/data/micro_ticket/dst/gao_tie/480p_2/319d48a27491d948ef288ce7664a03c0.mp4";

    /**
     * 下载文件
     *
     * @param downLoadTask
     */
    private void startDown(DownLoadTask downLoadTask) {

        if (TextUtils.isEmpty(downLoadTask.getDownLoadUrl())) {
            executeTask();
            Log.d(TAG, "下载地址为空 执行下一个任务！************************>");
            return;
        }
        Log.d(TAG, "##准备下载##：" + downLoadTask.getDownLoadUrl());

        long taskId = MyDownLoadManager.getDownLoadId(downLoadTask.getDownLoadUrl());
        DownLoadEntity DEntity = MyDownLoadManager.getDownLoadEntity(taskId);

        //过滤已完成的任务
        if (DEntity != null && DEntity.getCOLUMN_STATUS() == DownloadManager.STATUS_SUCCESSFUL) {
            executeTask();
            Log.d(TAG, "上条任务存在并且已经完成！************************>");
            return;
        }

        if (DEntity == null) {//任务不存在（断点续传）
            Log.d(TAG, "上条任务不存在！************************>" + isCurrentActivity);
            isHaseRealTask = true;
            DownloadManager.Request request = MyDownLoadManager.buildRequest(downLoadTask);

            taskId = MyDownLoadManager.getDownloadManager().enqueue(request);
            SharedPreferenceHelper.saveLong(downLoadTask.getDownLoadUrl(), taskId);

            if (isCurrentActivity) {
                //下载对话框
                showUpGradeDialog();
            }


        } else {
            Log.d(TAG, "上条任务已经存在！---------------------->");
        }

        //开始下载
        if (movieName != null) {
            movieName.setText(downLoadTask.getFileName());
        }

        long finalTaskId = taskId;

        Log.d(TAG, finalTaskId + "__________" + downLoadTask.toString());

        Disposable disposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .flatMap(count -> {
                    DownLoadEntity downLoadEntity = MyDownLoadManager.getDownLoadEntity(finalTaskId);
                    return Observable.just(downLoadEntity);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    float pro = message.getFormatDownLoadPregress();
                    Log.d("SSSSSSSSSS", "任务查询结果----状态--->" + MyDownLoadManager.getStatuDesc(message.getCOLUMN_STATUS()));
                    Log.d("SSSSSSSSSS", "任务查询结果----原因--->" + message.getCOLUMN_REASON());
                    Log.d("SSSSSSSSSS", "任务查询结果----存储地址--->" + message.getCOLUMN_LOCAL_URI());
                    if (isCurrentActivity) {
                        //下载对话框
                        showUpGradeDialog();
                    }
                    if (movieName != null) {
                        movieName.setText(downLoadTask.getFileName());
                    }
                    if (progressBar != null) {
                        progressBar.setProgress((int) pro);
                    }

                    if (loadingPercent != null) {
                        loadingPercent.setText(pro + "%");//设置进度条进度
                    }

                    Log.d(TAG, "下载进度: " + pro + "%");

                    switch (message.getCOLUMN_STATUS()) {
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            //是否需要通知服务器下载完成
//                            if (downLoadTask.isVideo() && downLoadTask.getId() != -1) {
//                                notifyServer(downLoadTask.getId());
//                            }

                            //取消任务
                            Disposable d = requestTaskMap.get(finalTaskId);
                            if (d != null) {
                                d.dispose();
                            }
                            requestTaskMap.remove(finalTaskId);

                            executeTask();
                        }

                        break;
                        case DownloadManager.STATUS_RUNNING: {
                            if (loadingState != null) {
                                loadingState.setText("正在下载");
                            }
                            String availableSize = SystemUtils.getSDAvailableSize(mActivity);

                            if (!TextUtils.isEmpty(availableSize)) {//当SD卡内存不足5M时
                                String sub = availableSize.replace(" GB", "");
                                double size1 = Double.parseDouble(sub);
                                if (size1 < 0.00488) {
                                    if (downLoadTask != null) {
                                        //取消任务
                                        Disposable dd = requestTaskMap.get(finalTaskId);
                                        if (dd != null) {
                                            dd.dispose();
                                        }
                                        requestTaskMap.remove(finalTaskId);
                                    }
                                    Toast.makeText(mActivity, "剩余空间不足", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        break;
                        case DownloadManager.STATUS_PENDING:
                            if (loadingState != null) {
                                loadingState.setText("等待下载");
                            }
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            if (loadingState != null) {
                                loadingState.setText("下载暂停");
                            }
                            break;
                        case DownloadManager.STATUS_FAILED:
                            if (loadingState != null) {
                                loadingState.setText("下载失败");
                            }
                            //取消任务
                            Disposable d = requestTaskMap.get(finalTaskId);
                            if (d != null) {
                                d.dispose();
                            }
                            requestTaskMap.remove(finalTaskId);

                            executeTask();
                            break;
                    }
                });

        requestTaskMap.put(taskId, disposable);
    }

    ProgressBar progressBar;
    TextView loadingPercent;
    TextView loadingState, title, movieName;
    AlertDialog dialog;

    private void showUpGradeDialog() {
        if (dialog == null) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_upgrade, null);
            progressBar = view.findViewById(R.id.progressbar);
            loadingPercent = view.findViewById(R.id.loadingPercent);
            loadingState = view.findViewById(R.id.loadingState);
            title = view.findViewById(R.id.down_load_title);
            movieName = view.findViewById(R.id.movie_name);
            dialog = new AlertDialog.Builder(mActivity).setView(view).setCancelable(false).create();

            loadingState.setOnClickListener(v -> {
                for (Map.Entry<Long, Disposable> entry : requestTaskMap.entrySet()) {
                    //取消查询任务
                    Disposable dd = entry.getValue();
                    if (dd != null) {
                        dd.dispose();
                    }
                }
                requestTaskMap.clear();
                dialog.dismiss();
            });
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 通知服务器下载完成了哪几个电影
     *
     * @param id 电影id
     */
    private void notifyServer(int id) {
        OkHttpUtils//
                .post()//
                .tag(this)//
                .url(Urls.reportSyncResult)//
                .addParams(AppConfig.token_key, AppConfig.token_value)//
                .addParams(AppConfig.requester_key, AppConfig.requester_value)//
                .addParams("imei", DeviceUtils.getUniqueId(mActivity))//移动设备IMEI
                .addParams("movieIds", id + "")//成功上传的电影ID,多个用逗号分隔
                .build()//
                .execute(new AppGsonCallback<BaseBean>(new RequestModel(mActivity).setShowProgress(false)) {
                    @Override
                    public void onResponseOK(BaseBean response, int id) {
                        super.onResponseOK(response, id);

                    }
                });
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(this);

        for (Map.Entry<Long, Disposable> entry : requestTaskMap.entrySet()) {
            //取消查询任务
            Disposable dd = entry.getValue();
            if (dd != null) {
                dd.dispose();
            }
        }
        requestTaskMap.clear();

        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);

        super.onDestroyView();
    }
}
