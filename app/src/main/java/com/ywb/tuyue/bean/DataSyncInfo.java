package com.ywb.tuyue.bean;

/**
 * Created by ph on 18-1-12.
 */

public class DataSyncInfo extends BaseBean {

    /**
     * result : {"hasNew":"1","lastSyncTime":{}}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String hasNew;
        private LastSyncTime lastSyncTime;

        public String getHasNew() {
            return hasNew;
        }

        public void setHasNew(String hasNew) {
            this.hasNew = hasNew;
        }

        public LastSyncTime getLastSyncTime() {
            return lastSyncTime;
        }

        public void setLastSyncTime(LastSyncTime lastSyncTime) {
            this.lastSyncTime = lastSyncTime;
        }
    }

    public static class LastSyncTime {
        long create_at;

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
        }
    }
}
