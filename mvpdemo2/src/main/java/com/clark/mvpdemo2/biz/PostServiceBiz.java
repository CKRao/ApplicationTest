package com.clark.mvpdemo2.biz;

import com.clark.mvpdemo2.bean.PostQueryInfo;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by clark on 2016/12/15.
 */

public interface PostServiceBiz {
    @POST("query")
    Observable<PostQueryInfo> searchRx(@Query("type") String type, @Query("postid") String postid);
}
