package com.clark.downloadtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clark.downloadtest.entities.FileInfo;
import com.clark.downloadtest.services.DownloadService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<FileInfo> mFileList;
    private ListView mListView;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        //创建文件集合
        mFileList = new ArrayList<>();
        //创建文件信息对象
        FileInfo fileInfo = new FileInfo(0, "http://dlsw.baidu.com/sw-search-sp/soft/4e/30195/Git_V2.5.1_64_bit_setup.1441791170.exe",
                "Git_V2.5.1_64_bit_setup.1441791170.exe", 0, 0);
        FileInfo fileInfo1 = new FileInfo(1, "http://183.2.192.174/imtt.dd.qq.com/16891/115604547451CDC19FB23E957DA1D475.apk?mkey=58b4fc3994e56684&f=9432&c=0&fsname=com.example.ckrao.myapplication_1.2.5_1.apk&csr=1bbd&p=.apk",
                "SpeedWeather.apk", 0, 0);
        FileInfo fileInfo2 = new FileInfo(2, "http://183.56.150.169/imtt.dd.qq.com/16891/2270ACE54E9894733854679B33B63DA8.apk?mkey=58b4fde194e56684&f=6d20&c=0&fsname=com.tencent.mm_6.5.4_1000.apk&csr=1bbd&p=.apk",
                "com.tencent.mm_6.5.4_1000.apk", 0, 0);
        FileInfo fileInfo3 = new FileInfo(3, "http://119.147.33.15/imtt.dd.qq.com/16891/32CB7178A596A9BF8A102BFECDF3A521.apk?mkey=58b4fdcd94e56684&f=6720&c=0&fsname=com.tencent.mobileqq_6.6.9_482.apk&csr=1bbd&p=.apk",
                "com.tencent.mobileqq_6.6.9_482.apk", 0, 0);
        mFileList.add(fileInfo);
        mFileList.add(fileInfo1);
        mFileList.add(fileInfo2);
        mFileList.add(fileInfo3);
        //创建适配器
        mAdapter = new ListAdapter(this, mFileList);
        //设置ListView
        mListView.setAdapter(mAdapter);
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        //        long time = System.currentTimeMillis();
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);
                mAdapter.updateProgress(id,finished);
            }else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())){
                //下载结束
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                //更新进度为零
                mAdapter.updateProgress(fileInfo.getId(),0);
                Toast.makeText(MainActivity.this,
                        mFileList.get(fileInfo.getId()).getFileName()+"下载完毕",
                        Toast.LENGTH_SHORT).show();


            }
        }
    };


}
