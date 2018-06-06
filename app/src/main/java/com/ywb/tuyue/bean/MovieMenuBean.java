package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/1/4.
 * description：
 */

public class MovieMenuBean extends BaseBean {


    /**
     * result : [{"name":"test6","update_at":1514362890000,"id":6},{"name":"test8","update_at":1514362849000,"id":8,"create_at":1514362483000,"status":"1"},{"name":"test2","update_at":1514362827000,"id":2,"status":"1"},{"name":"test7","update_at":1514362821000,"id":7,"create_at":1514361949000,"status":"1"}]
     */

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * name : test6
         * update_at : 1514362890000
         * id : 6
         * create_at : 1514362483000
         * status : 1
         */

        private String name;
        private long update_at;
        private int id;
        private long create_at;
        private String status;

        public ResultBean(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public ResultBean() {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
