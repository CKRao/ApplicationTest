package com.clark.mvpdemo2.model;

import com.clark.mvpdemo2.bean.PostQueryInfo;

/**
 * Created by clark on 2016/12/15.
 */

public interface PostSearchModel {
    void requestPostSearch(String type, String postid, PostSearchCallback callback);

    interface PostSearchCallback {
        void requestPostSearchSuccess(PostQueryInfo postQueryInfo);

        void requestPostSearchFail(String failStr);
    }
}
