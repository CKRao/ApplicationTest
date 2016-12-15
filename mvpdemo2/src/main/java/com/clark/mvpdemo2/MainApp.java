package com.clark.mvpdemo2;

import android.app.Application;

import com.clark.mvpdemo2.utils.RetrofitUtils;

/**
 * Created by clark on 2016/12/15.
 */

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtils.getInstance().initOkHttp(this);
    }
}
