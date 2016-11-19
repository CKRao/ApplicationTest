package com.clark.mvpdemo.view;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.clark.mvpdemo.R;
import com.clark.mvpdemo.adapter.ZhihuStoryAdapter;
import com.clark.mvpdemo.databeans.ZhihuStory;
import com.clark.mvpdemo.presenter.MainPresenter;
import com.clark.mvpdemo.view.IViewBind.IMainActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainActivity {
    ZhihuStoryAdapter mAdapter;
    RecyclerView mZhihudailylist;
    RelativeLayout mAcivitymain;
    ProgressBar mProgressBar;
    //将View与Presenter关联
    private MainPresenter mMainPresenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mZhihudailylist.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    private void initUI() {
        mZhihudailylist = (RecyclerView) findViewById(R.id.zhihudaily_list);
        mAcivitymain = (RelativeLayout) findViewById(R.id.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        mMainPresenter.loadData();
    }

    @Override
    public void getDataSuccess(ArrayList<ZhihuStory> stories) {
//不管界面怎么改只要与presenter进行绑定都得到的是stories数据，
// view界面只负责展示不关心怎么获取怎么处理解析数据
        if (mAdapter != null) {
            mAdapter.addItem(stories);
        } else {
            mAdapter = new ZhihuStoryAdapter(MainActivity.this, stories);
            mZhihudailylist.setAdapter(mAdapter);
        }
    }

    @Override
    public void getDataFail(String errCode, String errMsg) {
        Snackbar.make(mAcivitymain, errMsg, Snackbar.LENGTH_SHORT).show();
    }
}
