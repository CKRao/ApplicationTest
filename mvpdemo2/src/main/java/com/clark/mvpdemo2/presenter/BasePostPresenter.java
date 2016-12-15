package com.clark.mvpdemo2.presenter;

/**
 * Created by clark on 2016/12/15.
 */

public abstract class BasePostPresenter<T> {
    public T mView;

    public void attach(T view) {
        this.mView = view;
    }

    public void detach() {
        mView = null;
    }
}
