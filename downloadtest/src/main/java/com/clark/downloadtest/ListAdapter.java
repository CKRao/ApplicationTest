package com.clark.downloadtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clark.downloadtest.entities.FileInfo;
import com.clark.downloadtest.services.DownloadService;

import java.util.List;

import static com.clark.downloadtest.R.id.btStart;

/**
 * Created by clark on 2017/2/28.
 */

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileInfo> mFileList;

    public ListAdapter(Context context, List<FileInfo> fileList) {
        mContext = context;
        mFileList = fileList;
    }

    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final FileInfo fileInfo = mFileList.get(position);
        if (view == null) {
            //加载视图
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            //获取布局空间
            viewHolder = new ViewHolder();
            viewHolder.mFileName = (TextView) view.findViewById(R.id.tv);
            viewHolder.mFileProgressBar = (ProgressBar) view.findViewById(R.id.pbProgress);
            viewHolder.btStart = (Button) view.findViewById(btStart);
            viewHolder.btStop = (Button) view.findViewById(R.id.btStop);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mFileName.setText(fileInfo.getFileName());
        viewHolder.mFileProgressBar.setMax(100);
        viewHolder.mFileProgressBar.setProgress(fileInfo.getFinished());
        viewHolder.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TEST","Start");
                //通知Service开始下载
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", fileInfo);
                mContext.startService(intent);
            }
        });
        viewHolder.btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TEST","Stop");
                //通知Service停止下载
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", fileInfo);
                mContext.startService(intent);
            }
        });
        return view;
    }

    /**
     * 更新列表项的进度条
     */
    public void updateProgress(int id, int progress) {
        FileInfo fileInfo = mFileList.get(id);
        fileInfo.setFinished(progress);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView mFileName;
        ProgressBar mFileProgressBar;
        Button btStart, btStop;
    }
}
