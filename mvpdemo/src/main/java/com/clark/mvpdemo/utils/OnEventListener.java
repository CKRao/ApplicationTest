package com.clark.mvpdemo.utils;

/**
 * Created by clark on 2016/11/18.
 */

public interface OnEventListener<T> {
    void onSuccess(T response);
    void onFail(String errCode,String errMsg);
}
