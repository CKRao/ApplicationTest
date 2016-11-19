package com.clark.recycletest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by clark on 2016/11/3.
 */

public class TypeThreeHolder extends TypeAbstractViewHolder<DataModelThree> {
    public TextView content;
    public ImageView avatar;
    public TextView name;
    public ImageView contentImage;
    public TypeThreeHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        content = (TextView) itemView.findViewById(R.id.content);
        contentImage = (ImageView) itemView.findViewById(R.id.contentImage);
        itemView.setBackgroundResource(android.R.color.holo_blue_dark);
    }
    @Override
    public  void  bindHolder(DataModelThree model){
        avatar.setBackgroundResource(model.avatarColor);
        name.setText(model.name);
        content.setText(model.content);
        contentImage.setBackgroundResource(model.contentColor);
    }
}
