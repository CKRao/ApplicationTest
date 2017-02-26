package com.clark.downloadtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clark.downloadtest.entities.FileInfo;
import com.clark.downloadtest.services.DownloadService;

public class MainActivity extends AppCompatActivity {
    private TextView mFileName;
    private ProgressBar mPbProgress;
    private Button mBtStop;
    private Button mBtStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
        initView();
        //创建文件信息对象
        final FileInfo fileInfo = new FileInfo(0, "http://dlsw.baidu.com/sw-search-sp/soft/4e/30195/Git_V2.5.1_64_bit_setup.1441791170.exe",
                "Git_V2.5.1_64_bit_setup.1441791170.exe", 0, 0);
        mFileName.setText(fileInfo.getFileName());
        //添加时间监听
        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
            }
        });
        mBtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
            }
        });
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
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
//                if (System.currentTimeMillis() - time > 500) {
//                    time = System.currentTimeMillis();
                if (mPbProgress.getProgress() != finished) {

                    mPbProgress.setProgress(finished);
                }
            }
        }
    };

    private void initView() {
        mFileName = (TextView) findViewById(R.id.tv);
        mPbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        mBtStop = (Button) findViewById(R.id.btStop);
        mBtStart = (Button) findViewById(R.id.btStart);
        mPbProgress.setMax(100);
    }
}
