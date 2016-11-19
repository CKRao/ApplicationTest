package com.clark.mvpdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clark.mvpdemo.R;
import com.clark.mvpdemo.databeans.ZhihuStory;

import java.util.ArrayList;

/**
 * Created by clark on 2016/11/18.
 */

public class ZhihuStoryAdapter extends RecyclerView.Adapter {
    private ArrayList<ZhihuStory> mStories;
    private Context mContext;

    public ZhihuStoryAdapter(Context context, ArrayList<ZhihuStory> stories) {
        mContext = context;
        mStories = stories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zhihudaily_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.mNewsTitle.setText(mStories.get(position).getTitle());
        holder.mNewsImage.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mNewsImage;
        TextView mNewsTitle;

        ViewHolder(View view) {
            super(view);
            mNewsImage = (ImageView) view.findViewById(R.id.news_image);
            mNewsTitle = (TextView) view.findViewById(R.id.news_title);
        }
    }

    public void addItem(ArrayList<ZhihuStory> stories) {
        int position = mStories.size();
        mStories.addAll(stories);
        notifyItemInserted(position);
    }
}
