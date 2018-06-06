package com.ywb.tuyue.bean;

import java.io.Serializable;

/**
 * Created by mhdt on 2016/11/6.
 * update
 */

public class BaseBean implements Serializable {

    private String code;
    private String message;
    private int resultNum;

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseBean(String code, String message, int resultNum) {
        this.code = code;
        this.message = message;
        this.resultNum = resultNum;
    }

    public BaseBean() {

    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", resultNum=" + resultNum +
                '}';
    }
}
