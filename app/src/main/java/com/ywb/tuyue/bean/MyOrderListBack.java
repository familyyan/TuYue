package com.ywb.tuyue.bean;

import java.util.List;

/**
 * Created by penghao on 2018/1/6.
 * description：
 */

public class MyOrderListBack extends BaseBean {

    private List<MyOrderDetail> result;

    public List<MyOrderDetail> getResult() {
        return result;
    }

    public void setResult(List<MyOrderDetail> result) {
        this.result = result;
    }
}
