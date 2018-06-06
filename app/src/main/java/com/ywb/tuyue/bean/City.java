package com.ywb.tuyue.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghao on 2017/12/22.
 * description：
 */

public class City extends BaseBean implements Serializable {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * img_url : /file/wuhan.jpg
         * name : 武汉
         * update_at : 1514284984000
         * id : 1
         * create_at : 1514284984000
         * content : 武汉市是江城
         * status : 1
         */

        private String img_url;
        private String name;
        private String pinyin;
        private long update_at;
        private int id;
        private long create_at;
        private String content;
        private String contentPd;
        private String status;
        private String content_img;
        private String localPath;

        public String getContentPd() {
            return contentPd;
        }

        public void setContentPd(String contentPd) {
            this.contentPd = contentPd;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getContent_img() {
            return content_img;
        }

        public void setContent_img(String content_img) {
            this.content_img = content_img;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
