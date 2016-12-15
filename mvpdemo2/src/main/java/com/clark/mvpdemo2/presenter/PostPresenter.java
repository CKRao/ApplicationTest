package com.clark.mvpdemo2.presenter;

import android.util.Log;

import com.clark.mvpdemo2.bean.PostQueryInfo;
import com.clark.mvpdemo2.model.PostSearchModel;
import com.clark.mvpdemo2.model.PostSearchModelImpl;
import com.clark.mvpdemo2.view.MainView;

/**
 * Created by clark on 2016/12/15.
 */
public class PostPresenter extends BasePostPresenter<MainView> {
    private PostSearchModel mPostSearchModel;

    public PostPresenter(MainView mainView) {
        attach(mainView);
        mPostSearchModel = new PostSearchModelImpl();
    }

    public void requestHomeData(String type, String postid) {
        if (mPostSearchModel == null || mView == null) {
            return;
        }
        mView.showProgressDialog();
        mPostSearchModel.requestPostSearch(type, postid, new PostSearchModel.PostSearchCallback() {
            @Override
            public void requestPostSearchSuccess(PostQueryInfo postQueryInfo) {
                mView.hideProgressDialog();
                if (postQueryInfo!=null&&"ok".equals(postQueryInfo.getMessage()) ) {
//                    Log.i("Rao",postQueryInfo.toString());
                    mView.updateListUI(postQueryInfo);
                }
            }

            @Override
            public void requestPostSearchFail(String failStr) {
                mView.hideProgressDialog();
                mView.errorToast(failStr);
            }
        });
    }
}
