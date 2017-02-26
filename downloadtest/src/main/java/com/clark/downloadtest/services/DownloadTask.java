package com.clark.downloadtest.services;

import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;

import com.clark.downloadtest.db.ThreadDAO;
import com.clark.downloadtest.db.ThreadDAOImpl;
import com.clark.downloadtest.entities.FileInfo;
import com.clark.downloadtest.entities.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 下载任务类
 * Created by clark on 2017/2/26.
 */

public class DownloadTask {
    private Context mContext;
    private FileInfo mFileInfo;
    private ThreadDAO mDAO;
    private int mFinished = 0;
    public boolean isPause = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        mContext = context;
        mFileInfo = fileInfo;
        mDAO = new ThreadDAOImpl(mContext);
    }

    public void Download() {
        //读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDAO.getThreads(mFileInfo.getUrl());
        ThreadInfo threadInfo = null;
        if (threadInfos.size() == 0) {
            //初始化线程对象
            threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else {
            threadInfo = threadInfos.get(0);
        }
        //创建子线程下载
        new DownloadThread(threadInfo).start();
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;

        public DownloadThread(ThreadInfo threadInfo) {
            mThreadInfo = threadInfo;
        }
        HttpURLConnection conn = null;
        RandomAccessFile raf = null;
        InputStream input = null;
        @Override
        public void run() {
            //向数据库插入线程信息
            if (!mDAO.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
                mDAO.insertThread(mThreadInfo);
            }
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinished += mThreadInfo.getFinished();
                //开始下载
                if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1) {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
//                        if (System.currentTimeMillis() - time > 500) {
//                            time = System.currentTimeMillis();

                            intent.putExtra("finished", (int)((long)mFinished * 100 / (long)mFileInfo.getLength()));
                            mContext.sendBroadcast(intent);
//                        }
                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            mDAO.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            return;
                        }
                    }
                    mDAO.deleteThread(mThreadInfo.getUrl(), mThreadInfo.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
                try {
                    input.close();
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
