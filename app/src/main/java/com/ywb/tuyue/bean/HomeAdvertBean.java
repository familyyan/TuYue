package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/3/20.
 * description：
 */

public class HomeAdvertBean extends BaseBean {
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * img_url : https://www.yuwubao.cn:443/pictures/20180323/1521786643.png
         * update_at : 1521786653000
         * id : 39
         * title : 锁屏001
         * create_at : 1521458912000
         * advertType : stdby
         * content : <p>这是内容。</p><p><br></p>
         * status : 1
         */

        private String img_url;
        private long update_at;
        private int id;
        private String title;
        private long create_at;
        private String advertType;
        private String content;
        private String status;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
        }

        public String getAdvertType() {
            return advertType;
        }

        public void setAdvertType(String advertType) {
            this.advertType = advertType;
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


//
//    private List<ResultBean> result;
//
//    public List<ResultBean> getResult() {
//        return result;
//    }
//
//    public void setResult(List<ResultBean> result) {
//        this.result = result;
//    }
//
//    public static class ResultBean {
//        /**
//         * img_url : https://www.yuwubao.cn:443/pictures/20180319/1521458900.png
//         * update_at : 1521458912000
//         * id : 39
//         * title : 锁屏001
//         * create_at : 1521458912000
//         * advertType : stdby
//         * content : <p>这是内容。</p><p><br></p>
//         * status : 1
//         */
//
//        private String img_url;
//        private long update_at;
//        private int id;
//        private String title;
//        private long create_at;
//        private String advertType;
//        private String content;
//        private String status;
//
//        public String getImg_url() {
//            return img_url;
//        }
//
//        public void setImg_url(String img_url) {
//            this.img_url = img_url;
//        }
//
//        public long getUpdate_at() {
//            return update_at;
//        }
//
//        public void setUpdate_at(long update_at) {
//            this.update_at = update_at;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public long getCreate_at() {
//            return create_at;
//        }
//
//        public void setCreate_at(long create_at) {
//            this.create_at = create_at;
//        }
//
//        public String getAdvertType() {
//            return advertType;
//        }
//
//        public void setAdvertType(String advertType) {
//            this.advertType = advertType;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//    }
}
