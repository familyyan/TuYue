package com.zhy.http.okhttp;

/**
 * Created by mhdt on 2018/2/7.
 */

public class RequestErrorException extends Exception {
    public RequestErrorException(String message) {
        super(message);
    }
}
