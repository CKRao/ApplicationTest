package com.clark.mvpdemo2.view;

import com.clark.mvpdemo2.bean.PostQueryInfo;

/**
 * Created by clark on 2016/12/15.
 */

public interface MainView extends BaseView {

    void updateListUI(PostQueryInfo postQueryInfo);

    void errorToast(String message);
}
