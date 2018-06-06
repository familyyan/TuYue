package com.ywb.tuyue.bean;

import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

/**
 * Created by ph on 18-1-15.
 */
@SimpleTable(name = "t_movie")
public class MoviesListBean {

    /**
     * actor : 电影小图片测试
     * type_id : 8
     * director : 电影小图片测试
     * profile : 电影小图片测试
     * name : 电影小图片测试
     * download_url : http://47.93.158.24:8080/movie/20180112/1515748668.mp4
     * update_at : 1515654057000
     * poster_url : http://47.93.158.24:8080/pictures/20180111/1515653865.jpg
     * id : 40
     * detail : 电影小图片测试
     * create_at : 1515654057000
     * source_url : http://47.93.158.24:8080/movie/20180112/1515748668.mp4
     */

    @SimpleColumn(name = "actor")
    private String actor;//主演
    @SimpleColumn(name = "director")
    private String director;//导演
    @SimpleColumn(name = "profile")
    private String profile;
    @SimpleColumn(name = "name")
    private String name;//电影名字
    @SimpleId
    @SimpleColumn(name = "download_url")
    private String download_url;//下载地址
    @SimpleColumn(name = "update_at")
    private long update_at;//更新时间
    @SimpleColumn(name = "poster_url")
    private String poster_url;//海报下载地址
    @SimpleColumn(name = "id")
    private int id;//id
    @SimpleColumn(name = "detail")
    private String detail;//详情说明
    @SimpleColumn(name = "create_at")
    private long create_at;//创建时间
    @SimpleColumn(name = "source_url")
    private String source_url;//弃用
    @SimpleColumn(name = "file_path")
    private String file_path;//视频本地目录，弃用
    @SimpleColumn(name = "isFinish")
    private String isFinish;//是否下载完成，已不用
    @SimpleColumn(name = "type_id")
    private String type_id;//类型ID
    @SimpleColumn(name = "imagePath")
    private String imagePath;//图片存储地址

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
