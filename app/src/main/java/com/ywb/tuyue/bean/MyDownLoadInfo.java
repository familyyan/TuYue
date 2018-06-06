package com.ywb.tuyue.bean;

/**
 * Created by penghao on 2018/2/2.
 * description：
 */

public class MyDownLoadInfo {
    private String url, name;

    public MyDownLoadInfo(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
