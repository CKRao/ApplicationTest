package com.clark.mvpdemo.view.IViewBind;

import com.clark.mvpdemo.databeans.ZhihuStory;

import java.util.ArrayList;

/**
 * Created by clark on 2016/11/18.
 */

public interface IMainActivity {
    //显示进度条
    void showProgressBar();
    //隐藏进度条
    void hidProgressBar();
   //加载数据
    void loadData();
    //加载数据成功回调
    void getDataSuccess(ArrayList<ZhihuStory> stories);
    //加载数据失败回调
    void getDataFail(String errCode,String errMsg);
}
