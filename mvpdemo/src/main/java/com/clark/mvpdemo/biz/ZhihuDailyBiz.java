package com.clark.mvpdemo.biz;

import android.os.Handler;

import com.clark.mvpdemo.api.HttpServiceManager;
import com.clark.mvpdemo.databeans.ZhihuDaily;
import com.clark.mvpdemo.databeans.ZhihuStory;
import com.clark.mvpdemo.utils.OnEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by clark on 2016/11/18.
 */

public class ZhihuDailyBiz {
    //获取数据的具体实现方法
    public void getStoryData(final String url,
                             final OnEventListener<ArrayList<ZhihuStory>> eventListener) {
        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = HttpServiceManager.httpGet(url);
                    Gson gson = new Gson();
                    ZhihuDaily daily = gson.fromJson(result, ZhihuDaily.class);
                    final ArrayList<ZhihuStory> stories = daily.getStories();
                    if (stories != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                eventListener.onSuccess(stories);
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                eventListener.onFail("-100", "获取日报失败！");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            eventListener.onFail("-100", "获取日报失败！");
                        }
                    });
                }
            }
        }.start();

    }
}
