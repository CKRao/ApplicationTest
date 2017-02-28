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
import java.util.ArrayList;
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
    private int mThreadCount = 1;
    private List<DownloadThread> mThreadList;//线程集合

    public DownloadTask(Context context, FileInfo fileInfo, int threadCount) {
        mContext = context;
        mFileInfo = fileInfo;
        mThreadCount = threadCount;
        mDAO = new ThreadDAOImpl(mContext);
    }

    public void Download() {
        //读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDAO.getThreads(mFileInfo.getUrl());
        if (threadInfos.size() == 0) {
            //获得每个线程的下载长度
            int length = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(),
                        length * i, (i + 1) * length - 1, 0);
                if (i == mThreadCount - 1) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                //添加到集合中去
                threadInfos.add(threadInfo);
                //向数据库插入线程信息
                mDAO.insertThread(threadInfo);
            }
        }
        mThreadList = new ArrayList<>();

        //启动多个线程进行下载
        for (ThreadInfo threadInfo : threadInfos) {
            DownloadThread thread = new DownloadThread(threadInfo);
            thread.start();
            //添加线程到集合中去
            mThreadList.add(thread);

        }
    }

    /**
     * 判断是否所有线程都执行完成
     */
    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;
        //遍历线程集合，判断线程是否都执行完毕
        for (DownloadThread thread : mThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            //删除下载记录
            mDAO.deleteThread(mFileInfo.getUrl());
            //发送广播通知UI下载任务结束
            Intent intent = new Intent(DownloadService.ACTION_FINISHED);
            intent.putExtra("fileInfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;
        public boolean isFinished = false;

        public DownloadThread(ThreadInfo threadInfo) {
            mThreadInfo = threadInfo;
        }

        HttpURLConnection conn = null;
        RandomAccessFile raf = null;
        InputStream input = null;

        @Override
        public void run() {

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
                        //累加整个文件的进度
                        mFinished += len;
                        //累加每个线程的进度
                        mThreadInfo.setFinished((mThreadInfo.getStart()+mThreadInfo.getFinished() + len));
                        //把下载进度发送广播给Activity
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            intent.putExtra("finished",
                                    (int) ((long) mFinished * 100 / (long) mFileInfo.getLength()));
                            intent.putExtra("id", mFileInfo.getId());
                            mContext.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            mDAO.updateThread(mThreadInfo.getUrl(),
                                    mThreadInfo.getId(), mThreadInfo.getFinished());
                            return;
                        }
                    }
                    //标识线程执行完毕
                    isFinished = true;

                    //检查下载任务是否执行完毕
                    checkAllThreadFinished();
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
