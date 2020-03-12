package com.txtled.gp_a209.model;


import android.app.Activity;
import android.content.Context;

import com.txtled.gp_a209.model.db.DBHelper;
import com.txtled.gp_a209.model.net.NetHelper;
import com.txtled.gp_a209.model.operate.OperateHelper;
import com.txtled.gp_a209.model.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class DataManagerModel implements DBHelper, PreferencesHelper, NetHelper, OperateHelper {
    private DBHelper mDBDbHelper;
    private PreferencesHelper mPreferencesHelper;
    private NetHelper mNetHelper;
    private OperateHelper mOperateHelper;

    public DataManagerModel(DBHelper mDBDbHelper, PreferencesHelper
            mPreferencesHelper, NetHelper mNetHelper, OperateHelper mOperateHelper) {
        this.mDBDbHelper = mDBDbHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mNetHelper = mNetHelper;
        this.mOperateHelper = mOperateHelper;
    }

    @Override
    public boolean isFirstIn() {
        return mPreferencesHelper.isFirstIn();
    }

    @Override
    public void setFirstIn(boolean first) {
        mPreferencesHelper.setFirstIn(first);
    }

    @Override
    public String getUserId() {
        return mPreferencesHelper.getUserId();
    }

    @Override
    public void setUserId(String id) {
        mPreferencesHelper.setUserId(id);
    }

    @Override
    public void requestPermissions(Activity activity, String[] permissions, OnPermissionsListener permissionsListener) {
        mOperateHelper.requestPermissions(activity, permissions, permissionsListener);
    }
}
