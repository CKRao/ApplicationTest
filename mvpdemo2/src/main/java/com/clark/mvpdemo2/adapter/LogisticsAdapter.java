package com.clark.mvpdemo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clark.mvpdemo2.R;
import com.clark.mvpdemo2.bean.PostQueryInfo;
import com.clark.mvpdemo2.model.PostSearchModel;

import java.util.List;

/**
 * Created by clark on 2016/12/15.
 */

public class LogisticsAdapter extends BaseAdapter {
    private List<PostQueryInfo.DataBean> datas;
    private LayoutInflater mInflater;

    public LogisticsAdapter(Context context, List<PostQueryInfo.DataBean> datas) {
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        PostQueryInfo.DataBean dataBean = datas.get(position);
        if (convertView == null) {
            view = mInflater.inflate(R.layout.view_item_logistics,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.message = (TextView) view.findViewById(R.id.tv_conent);
            viewHolder.date = (TextView) view.findViewById(R.id.tv_date);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.message.setText(dataBean.getContext().replace("[", "【").replace("]", "】"));
        viewHolder.date.setText(dataBean.getTime());
        return view;
//        PostQueryInfo.DataBean data = datas.get(position);
//        convertView = mInflater.inflate(R.layout.view_item_logistics, null);
//        TextView tv_content= (TextView) convertView.findViewById(R.id.tv_conent);
//        TextView tv_date= (TextView) convertView.findViewById(R.id.tv_date);
//        tv_content.setText(data.getContext().replace("[","【").replace("]","】"));
//        tv_date.setText(data.getTime());
//        return convertView;
    }

    class ViewHolder {
        TextView message;
        TextView date;
    }
}
