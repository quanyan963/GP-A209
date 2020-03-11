package com.txtled.gp_a209.application;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;


import androidx.multidex.MultiDex;

import com.txtled.gp_a209.di.component.AppComponent;
import com.txtled.gp_a209.di.component.DaggerAppComponent;
import com.txtled.gp_a209.di.module.AppModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MyApplication extends Application {

    //private static ImageLoader mImageLoader;
    private static MyApplication sInstance;
    private List<Activity> mActivityList;
    private static AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (sInstance == null) {
            sInstance = this;
        }
        mActivityList = new ArrayList<>();
    }

    public static MyApplication getInstance() {
        if (sInstance == null) {
            return new MyApplication();
        } else {
            return sInstance;
        }
    }

    public static AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(sInstance))
                    .build();
        }
        return mAppComponent;
    }

    public void addActivity(Activity activity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity);
        }
    }


    public void removeAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }
}
