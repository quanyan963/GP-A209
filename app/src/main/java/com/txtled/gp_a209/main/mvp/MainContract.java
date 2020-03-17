package com.txtled.gp_a209.main.mvp;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;
import com.txtled.gp_a209.bean.DeviceInfo;

import java.util.List;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public interface MainContract {

    interface View extends BaseView {

        void getDeviceData(List<DeviceInfo> data);
    }

    interface Presenter extends BasePresenter<View> {

        void init();

        void onRefresh();
    }
}
