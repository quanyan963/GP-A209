package com.txtled.gp_a209.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.txtled.gp_a209.application.MyApplication;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class PreferencesHelperImpl implements PreferencesHelper {
    private static final String SP_NAME = "my_sp";
    private SharedPreferences mSharedPreferences;
    public static final String IS_FIRST_APP = "is_first_app";
    public static final String USER_ID = "user_id";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String UID = "uid";

    @Inject
    public PreferencesHelperImpl() {
        mSharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.
                MODE_PRIVATE);
    }

    @Override
    public boolean isFirstIn() {
        return mSharedPreferences.getBoolean(IS_FIRST_APP,true);
    }

    @Override
    public void setFirstIn(boolean first) {
        mSharedPreferences.edit().putBoolean(IS_FIRST_APP,first).apply();
    }

    @Override
    public String getUserId() {
        return mSharedPreferences.getString(USER_ID,"");
    }

    @Override
    public void setUserId(String id) {
        mSharedPreferences.edit().putString(USER_ID,id).apply();
    }

    @Override
    public String getDeviceAddress() {
        return mSharedPreferences.getString(DEVICE_ADDRESS,"");
    }

    @Override
    public void setDeviceAddress(String address) {
        mSharedPreferences.edit().putString(DEVICE_ADDRESS,address).apply();
    }

    @Override
    public void setUid(String uid) {
        mSharedPreferences.edit().putString(UID,uid).apply();
    }

    @Override
    public String getUid() {
        return mSharedPreferences.getString(UID,"");
    }
}
