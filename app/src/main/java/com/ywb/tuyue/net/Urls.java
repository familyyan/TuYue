package com.ywb.tuyue.net;

/**
 * Created by penghao on 2018/1/3.
 * description：
 */

public class Urls {

    public static boolean TEST_MODE = true;// 当前连接服务器模式，测试模式还是产线模式
    /**
     * 默认的API头地址
     */
    public static String TEST_HEAD_URL = "http://192.168.1.6:8080";//本地
    //    public static String ONLINE_HEAD_URL = "http://47.98.121.127:8080";//新环境
    public static String ONLINE_HEAD_URL = "http://192.168.100.123";//本地
    /**
     * 默认的图片API头地址
     */
    public static String TEST_HEAD_URL_IMAGE = "http://192.168.1.6";//本地
    //        public static String ONLINE_HEAD_URL = "http://47.98.121.127:8080";//新环境
    public static String ONLINE_HEAD_URL_IMAGE = "http://192.168.100.123";//本地
    /**
     * 1905片库头地址
     */
    public static String TEST_HEAD_URL_1905 = "http://192.168.1.6:8087";//本地
    //        public static String ONLINE_HEAD_URL = "http://47.98.121.127:8080";//新环境
    public static String ONLINE_HEAD_URL_1905 = "http://1.119.8.165:8087";//本地

    /**
     * 测试or生产
     */
    public static String HEAD_URL = TEST_MODE ? TEST_HEAD_URL : ONLINE_HEAD_URL;
    public static String HEAD_URL_IMAGE = TEST_MODE ? TEST_HEAD_URL_IMAGE : ONLINE_HEAD_URL_IMAGE;
    public static String HEAD_URL_1905 = TEST_MODE ? TEST_HEAD_URL_1905 : ONLINE_HEAD_URL_1905;
    //主页
    public static String homePage = HEAD_URL + "/tide/pd/homePage";
    //电影菜单
    public static String getMoviesConfigTypeList = HEAD_URL + "/tide/pd/getMoviesConfigTypeList";
    //某个菜单下的电影列表
    public static String getMoviesList = HEAD_URL + "/tide/pd/getMoviesList";
    //城市列表
    public static String getCityList = HEAD_URL + "/tide/pd/getCityList";
    //查询菜品分类列表
    public static String listFoodType = HEAD_URL + "/tide/pd/listFoodType";
    //根据类别查询菜品列表
    public static String listFoodByType = HEAD_URL + "/tide/pd/listFoodByType";
    //我的订单列表
    public static String myOrderList = HEAD_URL + "/tide/pd/myOrderList";
    //下单
    public static String submitOrder = HEAD_URL + "/tide/pd/submitOrder";
    //获取短信验证吗
    public static String sendVeriCode = HEAD_URL + "/tide/pd/sendVeriCode";
    //校验验证码
    public static String validateCode = HEAD_URL + "/tide/pd/validateCode";
    //城铁风采列表
    public static String getArticleList = HEAD_URL + "/tide/pd/getArticleList";
    //APP更新
    public static String selectVersionInfo = HEAD_URL + "/tide/pd/selectVersionInfo";
    //同步数据
    public static String getDataSyncInfo = HEAD_URL + "/tide/pd/getDataSyncInfo";
    //获取待同步的电影列表
    public static String getMoviesForSync = HEAD_URL + "/tide/pd/getMoviesForSync";
    //汇报下载成功结果
    public static String reportSyncResult = HEAD_URL + "/tide/pd/reportSyncResult";
    //获取解锁广告位背景图
    public static String getStdbyAdvert = HEAD_URL + "/tide/pd/getStdbyAdvert";
    //获取富文本中的资源文件
    public static String getResourceFile = HEAD_URL + "/tide/pd/getResourceFile";
    //点餐大列表
    public static String listAllFood = HEAD_URL + "/tide/pd/listAllFood";
    //电玩城大列表
    public static String getGames = HEAD_URL + "/tide/pd/getGames";
    //书吧大列表
    public static String getBooks = HEAD_URL + "/tide/pd/getBooks";
    //音乐大列表
    public static String getMusic = HEAD_URL + "/tide/pd/getMusic";
    //上传统计数据
//    public static String sync = HEAD_URL + "/tide/pd/sync";
    public static String sync = "http://47.98.121.127/app/sync";
//    public static String sync = "http://192.168.100.123:8883/sync";

    public static String get1905Url = HEAD_URL_1905 + "/index.php/Home/Interface/index?class=HallUse&method=getVideoList";

    public static void change() {

        TEST_HEAD_URL = "http://192.168.1.6";//本地
        ONLINE_HEAD_URL = "http://47.93.158.24";//新环境


        homePage = HEAD_URL + "/tide/pd/homePage";
        getMoviesConfigTypeList = HEAD_URL + "/tide/pd/getMoviesConfigTypeList";
        getMoviesList = HEAD_URL + "/tide/pd/getMoviesList";
        getCityList = HEAD_URL + "/tide/pd/getCityList";
        listFoodType = HEAD_URL + "/tide/pd/listFoodType";
        listFoodByType = HEAD_URL + "/tide/pd/listFoodByType";
        myOrderList = HEAD_URL + "/tide/pd/myOrderList";
        submitOrder = HEAD_URL + "/tide/pd/submitOrder";
        sendVeriCode = HEAD_URL + "/tide/pd/sendVeriCode";
        validateCode = HEAD_URL + "/tide/pd/validateCode";
        getArticleList = HEAD_URL + "/tide/pd/getArticleList";
        selectVersionInfo = HEAD_URL + "/tide/pd/selectVersionInfo";
        getDataSyncInfo = HEAD_URL + "/tide/pd/getDataSyncInfo";
        getMoviesForSync = HEAD_URL + "/tide/pd/getMoviesForSync";
        reportSyncResult = HEAD_URL + "/tide/pd/reportSyncResult";
        getStdbyAdvert = HEAD_URL + "/tide/pd/getStdbyAdvert";
        getResourceFile = HEAD_URL + "/tide/pd/getResourceFile";
        listAllFood = HEAD_URL + "/tide/pd/listAllFood";
        getGames = HEAD_URL + "/tide/pd/getGames";
        getBooks = HEAD_URL + "/tide/pd/getBooks";
        getMusic = HEAD_URL + "/tide/pd/getMusic";
        sync = "http://47.98.121.127/app/sync";
        get1905Url = HEAD_URL_1905 + "/index.php/Home/Interface/index?class=HallUse&method=getVideoList";
    }
}
