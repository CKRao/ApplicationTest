package com.clark.recycletest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by clark on 2016/11/3.
 */

public class TypeTwoHolder extends TypeAbstractViewHolder<DataModelTwo> {
    public TextView content;
    public ImageView avatar;
    public TextView name;
    public TypeTwoHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        content = (TextView) itemView.findViewById(R.id.content);
        itemView.setBackgroundResource(android.R.color.darker_gray);
    }
    @Override
    public  void  bindHolder(DataModelTwo model){
        avatar.setBackgroundResource(model.avatarColor);
        name.setText(model.name);
        content.setText(model.content);
    }
}
