package com.ywb.tuyue.db.dao;


import com.simple.util.db.operation.TemplateDAO;
import com.ywb.tuyue.AppContext;
import com.ywb.tuyue.bean.MovieSyncDataBean;
import com.ywb.tuyue.db.DBHelper;

import java.util.List;


/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by:
 */
public class BannerDao extends TemplateDAO<MovieSyncDataBean.ResultBean.MovieBannerBean> {
    private static BannerDao instance;

    public BannerDao() {
        super(new DBHelper(AppContext.getApplication()));
    }

    public static BannerDao getInstance() {
        if (instance == null) {
            synchronized (BannerDao.class) {
                if (instance == null) {
                    instance = new BannerDao();
                }
            }
        }
        return instance;
    }

    public MovieSyncDataBean.ResultBean.MovieBannerBean getLastBanner() {
        List<MovieSyncDataBean.ResultBean.MovieBannerBean> banners = find();
        if (banners != null && !banners.isEmpty()) {
            return banners.get(banners.size() - 1);
        } else {
            return null;
        }
    }

    public List<MovieSyncDataBean.ResultBean.MovieBannerBean> getAllBanner() {
        List<MovieSyncDataBean.ResultBean.MovieBannerBean> banners = find();
        if (banners != null) {
            return banners;
        } else {
            return null;
        }
    }

    public void removeOne(int id) {
        delete(id);
    }

    public void updateBanner(MovieSyncDataBean.ResultBean.MovieBannerBean bannerBean) {
        if (get(bannerBean.getId()) == null) {
            insert(bannerBean);
        } else {
            update(bannerBean);
        }
    }

    public MovieSyncDataBean.ResultBean.MovieBannerBean getObjectFromId(int id) {
        if (get(id) != null) {
            return get(id);
        } else {
            return null;
        }
    }

}
