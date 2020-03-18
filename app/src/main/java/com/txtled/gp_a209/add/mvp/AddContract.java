package com.txtled.gp_a209.add.mvp;

import android.app.Activity;
import android.net.wifi.WifiInfo;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public interface AddContract {
    interface View extends BaseView{

        void hidSnackBar();

        void checkLocation();

        void setNoWifiView();

        void setInfo(String ssid);

        void showConnectHint();

        void configureSuccess(String address);

        void setData(String[] data);
    }

    interface Presenter extends BasePresenter<View>{

        void init(Activity activity);

        void onClick(int id);

        void configWifi(byte[] s, String pass);

        void connDevice(String address);

        void setName(String name);

        void getConfiguredData();
    }
}
