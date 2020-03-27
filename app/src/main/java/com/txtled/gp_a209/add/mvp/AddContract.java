package com.txtled.gp_a209.add.mvp;

import android.app.Activity;
import android.net.wifi.WifiInfo;

import com.txtled.gp_a209.add.listener.OnCreateThingListener;
import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;
import com.txtled.gp_a209.bean.WWADeviceInfo;

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

        void configureSuccess();

        void setData(String[] data);

        void onStatueChange();

        void dismiss();

        void changeName(String name);

        void showSuccess();
    }

    interface Presenter extends BasePresenter<View>{

        void init(Activity activity);

        void onClick(int id, boolean isCommit);

        void configWifi(byte[] s, String pass);

        void setName(String name);

        void getConfiguredData();

        void getDeviceInfo();

        void onDestroy();

        void initData(WWADeviceInfo info);
    }
}
