package com.clark.recycletest;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by clark on 2016/11/3.
 */

public abstract class TypeAbstractViewHolder<T> extends RecyclerView.ViewHolder {
    public TypeAbstractViewHolder(View itemView) {
        super(itemView);
    }
    public abstract void bindHolder(T model);
}
