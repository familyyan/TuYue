package com.ywb.tuyue.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mhdt on 2017/12/16.
 * 电影
 */

public class Movie extends BaseBean implements Serializable {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * click_amount : 0
         * type_id : 6
         * director : fdsafff
         * profile : fdsaffd
         * poster_url : https://www.yuwubao.cn:443/pictures/20180104/1515051272.png
         * source_url : fsfafsafsaf
         * actor : fdsafdfdsfsf
         * name : fdsafsfsfaf11111
         * download_url : sfsfaf
         * update_at : 1515051284000
         * id : 25
         * detail : dsfsaff
         * create_at : 1515051284000
         * status : 1
         */

        private int click_amount;
        private String type_id;
        private String director;
        private String profile;
        private String poster_url;
        private String source_url;
        private String actor;
        private String name;
        private String download_url;
        private long update_at;
        private int id;
        private String detail;
        private long create_at;
        private String status;
        private String filePath;//本地目录
        private String localPath;//图片的本地目录

        public ResultBean(String profile, String poster_url, String name, String source_url, String actor, String director, String filePath) {
            this.profile = profile;
            this.poster_url = poster_url;
            this.name = name;
            this.source_url = source_url;
            this.actor = actor;
            this.director = director;
            this.filePath = filePath;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getClick_amount() {
            return click_amount;
        }

        public void setClick_amount(int click_amount) {
            this.click_amount = click_amount;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
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

        public String getPoster_url() {
            return poster_url;
        }

        public void setPoster_url(String poster_url) {
            this.poster_url = poster_url;
        }

        public String getSource_url() {
            return source_url;
        }

        public void setSource_url(String source_url) {
            this.source_url = source_url;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "click_amount=" + click_amount +
                    ", type_id='" + type_id + '\'' +
                    ", director='" + director + '\'' +
                    ", profile='" + profile + '\'' +
                    ", poster_url='" + poster_url + '\'' +
                    ", source_url='" + source_url + '\'' +
                    ", actor='" + actor + '\'' +
                    ", name='" + name + '\'' +
                    ", download_url='" + download_url + '\'' +
                    ", update_at=" + update_at +
                    ", id=" + id +
                    ", detail='" + detail + '\'' +
                    ", create_at=" + create_at +
                    ", status='" + status + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", localPath='" + localPath + '\'' +
                    '}';
        }
    }
}
