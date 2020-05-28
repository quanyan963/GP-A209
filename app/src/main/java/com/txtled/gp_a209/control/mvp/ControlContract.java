package com.txtled.gp_a209.control.mvp;

import android.app.Activity;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;
import com.txtled.gp_a209.bean.IotCoreData;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public interface ControlContract {
    interface View extends BaseView{

        void mqttSuccess(int id);

        void mqttFail(int id);

        void hidLoadingView();

        void setData(IotCoreData iotCoreData);

        void powerChanged(boolean power);

        void volumeFail(int progress);

        void lightFail();

        void initFail();

        void resetView(IotCoreData iotCoreData);
    }

    interface Presenter extends BasePresenter<View>{
        void init(String endpoint, Activity activity);

        void sendMqtt(int id);

        void sendLight(int state);

        void onClick(int id,boolean power);

        void initData();

        void sendVolume(int progress);

        void destroy();

        void enableView();
    }
}
