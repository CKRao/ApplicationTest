package com.clark.recycletest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by clark on 2016/11/3.
 */

public class TypeOneHolder extends TypeAbstractViewHolder<DataModelOne> {
    public ImageView avatar;
    public TextView name;
    public TypeOneHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        itemView.setBackgroundResource(android.R.color.black);
    }
    @Override
    public  void  bindHolder(DataModelOne model){
        avatar.setBackgroundResource(model.avatarColor);
        name.setText(model.name);
    }
}
