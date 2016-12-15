package com.clark.mvpdemo2.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.clark.mvpdemo2.view.BaseView;

/**
 * Created by clark on 2016/12/15.
 */

public class BaseActivity extends Activity implements BaseView {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("查询中...");
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
