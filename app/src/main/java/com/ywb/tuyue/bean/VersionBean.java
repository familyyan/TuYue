package com.ywb.tuyue.bean;

/**
 * Created by penghao on 2018/1/8.
 * description：
 */

public class VersionBean extends BaseBean {


    /**
     * result : {"Version":"版本名称 V1.2","lastUpdate":1515402127000,"downloadUrl":"下载URL","id":5,"apkSize":"50.6MB","versionNumber":"1.2","content":"更新内容"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * Version : 版本名称 V1.2
         * lastUpdate : 1515402127000
         * downloadUrl : 下载URL
         * id : 5
         * apkSize : 50.6MB
         * versionNumber : 1.2
         * content : 更新内容
         */

        private String Version;
        private long lastUpdate;
        private String downloadUrl;
        private int id;
        private String apkSize;
        private String versionNumber;
        private String content;

        public String getVersion() {
            return Version;
        }

        public void setVersion(String Version) {
            this.Version = Version;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getApkSize() {
            return apkSize;
        }

        public void setApkSize(String apkSize) {
            this.apkSize = apkSize;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
