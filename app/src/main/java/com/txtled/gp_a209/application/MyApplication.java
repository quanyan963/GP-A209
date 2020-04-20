package com.txtled.gp_a209.application;

import android.app.Activity;
import android.app.Application;

import androidx.multidex.MultiDex;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.txtled.gp_a209.di.component.AppComponent;
import com.txtled.gp_a209.di.component.DaggerAppComponent;
import com.txtled.gp_a209.di.module.AppModule;

import java.util.ArrayList;
import java.util.List;

import static com.txtled.gp_a209.utils.Constants.IDENTITY_POOL_ID;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MyApplication extends Application {

    //private static ImageLoader mImageLoader;
    private static MyApplication sInstance;
    private List<Activity> mActivityList;
    private static AppComponent mAppComponent;
    private static final Regions MY_REGION = Regions.US_EAST_1;
    private static CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (sInstance == null) {
            sInstance = this;
        }
        mActivityList = new ArrayList<>();

        // 初始化 Amazon Cognito 凭证提供程序
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                IDENTITY_POOL_ID, // 身份池 ID
                MY_REGION // 区域
        );
    }

    public static CognitoCachingCredentialsProvider getCredentialsProvider(){
        return credentialsProvider;
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
