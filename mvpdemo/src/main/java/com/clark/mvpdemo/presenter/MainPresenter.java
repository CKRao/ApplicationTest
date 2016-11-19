package com.clark.mvpdemo.presenter;

import android.content.Context;

import com.clark.mvpdemo.biz.ZhihuDailyBiz;
import com.clark.mvpdemo.databeans.ZhihuStory;
import com.clark.mvpdemo.utils.OnEventListener;
import com.clark.mvpdemo.view.IViewBind.IMainActivity;
import com.clark.mvpdemo.view.MainActivity;

import java.util.ArrayList;

/**
 * Created by clark on 2016/11/18.
 */

public class MainPresenter {
    private ZhihuDailyBiz mDailyBiz;
    private IMainActivity mIMainActivity;

    public MainPresenter(IMainActivity iMainActivity) {
        //绑定获得View对象
        mIMainActivity = iMainActivity;
        //绑定获得业务实现对象
        mDailyBiz = new ZhihuDailyBiz();
    }

    //对View提供的调用方法
    public void loadData() {
        mIMainActivity.showProgressBar();
        mDailyBiz.getStoryData("news/latest", new OnEventListener<ArrayList<ZhihuStory>>() {
            @Override
            public void onSuccess(ArrayList<ZhihuStory> response) {
                mIMainActivity.hidProgressBar();
                mIMainActivity.getDataSuccess(response);
            }

            @Override
            public void onFail(String errCode, String errMsg) {
                mIMainActivity.hidProgressBar();
                mIMainActivity.getDataFail(errCode, errMsg);
            }
        });
    }

}
