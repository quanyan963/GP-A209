package com.txtled.gp_a209.login.mvp;

import android.app.Activity;
import android.net.wifi.WifiInfo;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/12.
 */
public interface LoginContract {
    interface View extends BaseView {

        void hidLoadingView();

        void showLoadingView();

        void checkLocation();

        void setNoWifiView();

        void hidSnackBar();

        void setInfo(String ssid, WifiInfo info);

        void showLoginFail();

        void toMainView();
    }
    interface Presenter extends BasePresenter<View>{

        void init(Activity activity);

        void viewClick(int id);

        void onResume(boolean isShowing);

        void onDestroy(Activity activity);
    }
}
