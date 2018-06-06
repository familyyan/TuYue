package com.ywb.tuyue.db;

import android.content.Context;

import com.simple.util.db.operation.SimpleDbHelper;
import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.bean.Count;
import com.ywb.tuyue.bean.Food;
import com.ywb.tuyue.bean.MovieSyncDataBean;
import com.ywb.tuyue.bean.MoviesListBean;
import com.ywb.tuyue.bean.User;

/**
 * 数据库的帮助类
 *
 * @author mhdt
 * @version 1.0
 * @created 2016-7-29 上午10:44:35
 * @update
 */
public class DBHelper extends SimpleDbHelper {

    private static final String DBNAME = AppConfig.APP_DBNAME;
    private static final int DBVERSION = AppConfig.APP_DBVERSION;
    private static final Class<?>[] clazz = {User.class, Food.ResultBean.class, MoviesListBean.class, MovieSyncDataBean.ResultBean.MovieBannerBean.class, Count.class};
    public DBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION, clazz);
    }
}