package com.ywb.tuyue.bean;

/**
 * Created by penghao on 2018/4/11.
 * description：
 */

public class RichTextBean extends BaseBean {


    /**
     * result : {"allFileUrl":"https://www.yuwubao.cn:443/pictures/20180411/1523416402.jpg,https://www.yuwubao.cn:443/pictures/20180411/1523416391.jpg,https://www.yuwubao.cn:443/pictures/20180412/1523531540.jpg,https://www.yuwubao.cn:443/pictures/20180409/1523241128.jpg,,","time":"2018-04-13 15:29:56"}
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
         * allFileUrl : https://www.yuwubao.cn:443/pictures/20180411/1523416402.jpg,https://www.yuwubao.cn:443/pictures/20180411/1523416391.jpg,https://www.yuwubao.cn:443/pictures/20180412/1523531540.jpg,https://www.yuwubao.cn:443/pictures/20180409/1523241128.jpg,,
         * time : 2018-04-13 15:29:56
         */

        private String allFileUrl;
        private String time;

        public String getAllFileUrl() {
            return allFileUrl;
        }

        public void setAllFileUrl(String allFileUrl) {
            this.allFileUrl = allFileUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
