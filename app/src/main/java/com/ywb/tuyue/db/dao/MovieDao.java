package com.ywb.tuyue.db.dao;


import com.simple.util.db.operation.TemplateDAO;
import com.ywb.tuyue.AppContext;
import com.ywb.tuyue.bean.MoviesListBean;
import com.ywb.tuyue.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * function
 * Created by mhdt on 2016/12/3.12:38
 * update by:
 */
public class MovieDao extends TemplateDAO<MoviesListBean> {
    private static MovieDao instance;

    public MovieDao() {
        super(new DBHelper(AppContext.getApplication()));
    }

    public static MovieDao getInstance() {
        if (instance == null) {
            synchronized (MovieDao.class) {
                if (instance == null) {
                    instance = new MovieDao();
                }
            }
        }
        return instance;
    }

    public List<MoviesListBean> getMovieList() {
        List<MoviesListBean> movies = find();
        if (movies != null && movies.size() > 0) {
            return movies;
        } else {
            return null;
        }
    }

    public List<String> getMovieListUrls() {
        List<String> urls = new ArrayList<>();
        List<MoviesListBean> movies = find();
        if (movies != null && !movies.isEmpty()) {
            for (MoviesListBean movie : movies) {
                urls.add(movie.getDownload_url());
            }
            return urls;
        } else {
            return null;
        }
    }

    public void removeOne(String url) {
        delete("download_url = ?", new String[]{url});
    }

    public void updateMovie(MoviesListBean moviesListBean) {
        if (get(moviesListBean.getDownload_url()) == null) {
            insert(moviesListBean);
        } else {
            update(moviesListBean);
        }
    }

    public MoviesListBean getObjectFromUrl(String url) {
        if (get(url) != null) {
            return get(url);
        } else {
            return null;
        }
    }

    public List<MoviesListBean> getMoviesByType(String type) {

        List<MoviesListBean> list = rawQuery("select * from t_movie where type_id like ? ", new String[]{"%" + type + "%"});
        if (null != list && !list.isEmpty()) {
            return list;
        } else {
            return null;
        }
    }


}
