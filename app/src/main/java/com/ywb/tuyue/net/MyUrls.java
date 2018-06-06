package com.ywb.tuyue.net;

/**
 * Created by penghao on 2018/1/3.
 * description：
 */

public class MyUrls {

    public static boolean TEST_MODE = false;// 当前连接服务器模式，测试模式还是产线模式
    /**
     * 默认的API头地址
     */
    public static String TEST_HEAD_URL = "http://192.168.1.6:8080/tide/";//本地
    //public static String ONLINE_HEAD_URL = "http://47.93.158.24:8080/tide/";//新环境
    public static String ONLINE_HEAD_URL = "http://47.97.176.92:8080/tide/";//新环境
    /**
     * 测试or生产
     */
    public static String HEAD_URL = TEST_MODE ? TEST_HEAD_URL : ONLINE_HEAD_URL;
    //主页
    public static String homePage = HEAD_URL + "pd/local/homePage";
    //电影菜单
    public static String getMoviesConfigTypeList = HEAD_URL + "pd/local/getMoviesConfigTypeList";
    //某个菜单下的电影列表
    public static String getMoviesList = HEAD_URL + "pd/local/getMoviesList";
    //城市列表
    public static String getCityList = HEAD_URL + "pd/local/getCityList";
    //查询菜品分类列表
    public static String listFoodType = HEAD_URL + "pd/local/listFoodType";
    //根据类别查询菜品列表
    public static String listFoodByType = HEAD_URL + "pd/local/listFoodByType";
    //我的订单列表
    public static String myOrderList = HEAD_URL + "pd/local/myOrderList";
    //下单
    public static String submitOrder = HEAD_URL + "pd/local/submitOrder";
    //获取短信验证吗
    public static String sendVeriCode = HEAD_URL + "pd/local/sendVeriCode";
    //校验验证码
    public static String validateCode = HEAD_URL + "pd/local/validateCode";
    //城铁风采列表
    public static String getArticleList = HEAD_URL + "pd/local/getArticleList";
    //APP更新
    public static String selectVersionInfo = HEAD_URL + "pd/local/selectVersionInfo";
    //同步数据
    public static String getDataSyncInfo = HEAD_URL + "pd/local/getDataSyncInfo";
    //获取待同步的电影列表
    public static String getMoviesForSync = HEAD_URL + "pd/local/getMoviesForSync";
    //汇报下载成功结果
    public static String reportSyncResult = HEAD_URL + "pd/local/reportSyncResult";
    //获取解锁广告位背景图
    public static String getStdbyAdvert = HEAD_URL + "pd/local/getStdbyAdvert";
    //获取富文本中的资源文件
    public static String getResourceFile = HEAD_URL + "pd/local/getResourceFile";
    //点餐大列表
    public static String listAllFood = HEAD_URL + "pd/local/listAllFood";
    //电玩城大列表
    public static String getGames = HEAD_URL + "pd/local/getGames";
    //书吧大列表
    public static String getBooks = HEAD_URL + "pd/local/getBooks";
    //音乐大列表
    public static String getMusic = HEAD_URL + "pd/local/getMusic";
}
