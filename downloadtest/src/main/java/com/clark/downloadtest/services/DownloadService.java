package com.clark.downloadtest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.clark.downloadtest.entities.FileInfo;

import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clark on 2017/2/26.
 */

public class DownloadService extends Service {

    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/downloads/";
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    public static final int MSG_INIT = 0;
    //下载任务的集合
    private Map<Integer, DownloadTask> mTasks = new LinkedHashMap<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得Acyivity传来的参数
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
//            Log.i("test", "Start:" + fileInfo.toString());
            //启动初始化线程
                new InitThread(fileInfo).start();
        } else if (ACTION_STOP.equals(intent.getAction())) {
            //暂停下载
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            DownloadTask task = mTasks.get(fileInfo.getId());
            //从集合中取出下载任务
            if (task != null) {
                //停止下载任务
                task.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }


    /**
     * 初始化子线程
     */
    class InitThread extends Thread {
        private FileInfo mFileInfo;

        public InitThread(FileInfo fileInfo) {
            mFileInfo = fileInfo;
        }

        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_INIT:
                        FileInfo fileInfo = (FileInfo) msg.obj;
                        Log.i("test", "Init:" + fileInfo);
                        //启动下载任务
                        DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 3);
                        task.Download();
                        //把下载任务添加到集合中
                        mTasks.put(fileInfo.getId(), task);
                        break;
                }
            }
        };

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile raf = null;
            try {
                //链接网络文件
                URL url = new URL(mFileInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                int length = -1;
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //获取文件长度
                    length = connection.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //在本地创建文件
                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                //设置文件长度
                raf.setLength(length);
                mFileInfo.setLength(length);
                mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
