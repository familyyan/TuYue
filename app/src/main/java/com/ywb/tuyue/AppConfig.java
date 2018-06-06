package com.ywb.tuyue;

import android.os.Environment;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by Peng on 2017/8/30
 * e-mail: phlxplus@163.com
 * description:应用程序配置类：用于保存用户相关信息及设置
 */

public class AppConfig {
    public static final String APP_DBNAME = "tuyue.db";
    public static final int APP_DBVERSION = 13;
    public static final String APP_SHAPENAME = "tuyue";
    public static final String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory()
            .getPath();
    public static final String APP_PACKAGE_PATH = "com.ywb.tuyue";

    public static String sdRootPath = SDCARD_ROOT_PATH;
    public static String appCenterPath = "/com.ywb.tuyue/";
    public static final String FILEPATH = File.separator + APP_PACKAGE_PATH + File.separator;
    // 文件默认存放地址
    public final static String DEFAULT_SAVE_PATH = sdRootPath + appCenterPath;
    public static boolean isChecked;// 本次是否检查过版本更新

    public static long lastPressTime;
    public static long exitduration = 2000;
    public static final String ISLOGINKEY = "isloginkey";

    public static final String ISAUTOLOGOIN = "isAutoLogin";
    //--
    public static final String STARTCOUNT = "startCount";
    public static final String openCount = "openCount1";
    public static final String startCountTest = "startCountTest";
    public static final String switch_btn_state = "switch_btn_state";

    //--公共请求参数--
    public static final String token_key = "X-Authorization";
    public static final String token_value = "Bearer ";
    public static final String requester_key = "X-Requester";
    public static final String requester_value = "pd";
    public static final String page = "page";
    public static final String pageSize = "pageSize";


    //--返回码值
    public static final String RESULT_OK = "000000";//成功
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 请求是否成功
     *
     * @param code
     * @return
     */
    public static boolean isRequestOk(String code) {
        return RESULT_OK.equals(code);
    }


    //--
    public static final String ISCHECKVERSION = "isCheckVersion";
    public static final String LASTVERSIONCODE = "LASTVERSIONCODE";// 上一次更新的版本id
    public static final String LASTVERSIONSIZE = "LASTVERSIONSIZE";// 更新版本的apk大小
    public static final String NEWVERSIONCODE = "NEWVERSIONCODE";// 最新的版本
    //wifi
    public static final String WIFI_STATE_CONNECT = "已连接";
    public static final String WIFI_STATE_ON_CONNECTING = "正在连接";
    public static final String WIFI_STATE_UNCONNECT = "未连接";

    //下载后的各类数据
    public static final String CITY_DATA = "CITY_DATA";
    public static final String GETARTICLELIST = "GETARTICLELIST";
    public static final String HOMEPAGE = "HOMEPAGE";
    public static final String GETSTDBYADVERT = "GETSTDBYADVERT";
    public static final String GETMOVIESCONFIGTYPELIST = "GETMOVIESCONFIGTYPELIST";
    public static final String GETRESOURCEFILE = "GETRESOURCEFILE";
    public static final String LISTALLFOOD = "LISTALLFOOD";
    public static final String GAME_DATA = "GAME_DATA";
    public static final String BOOK_DATA = "BOOK_DATA";
    public static final String MUSIC_DATA = "MUSIC_DATA";
    public static final String MOVIEBANNER = "MOVIEBANNER";
    public static final String TIME = "TIME";

    //receiver action
    public static final String ACTION_SERVICE_RECEIVER = "ACTION_SERVICE_RECEIVER";
    public static final String ACTION_ACTIVITY_RECEIVER = "ACTION_ACTIVITY_RECEIVER";


    public static final String TYPE_ID_1905 = "videos";



}
