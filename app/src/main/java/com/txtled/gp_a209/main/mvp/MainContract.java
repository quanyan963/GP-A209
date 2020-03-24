package com.txtled.gp_a209.main.mvp;

import android.app.Activity;
import android.view.View;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public interface MainContract {

    interface View extends BaseView {

        void getDeviceData(List<DeviceInfo> data);

        void hidSnackBar();

        void checkLocation();

        void setNoWifiView();

        void closeRefresh();

        void setData(List<WWADeviceInfo> refreshData);

        void noDevice();

        void getWifiName(String ssid);

        void deleteError();

        void deleteSuccess();

        void mqttInitFail();

        void success(boolean allOff);

        void fail();

        void showLoading();
    }

    interface Presenter extends BasePresenter<View> {

        String init(Activity activity);

        void onRefresh();

        void deleteDevice(WWADeviceInfo data, String name);

        void onClick(android.view.View v);
    }
}
