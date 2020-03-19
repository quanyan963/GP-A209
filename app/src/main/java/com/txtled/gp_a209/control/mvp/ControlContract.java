package com.txtled.gp_a209.control.mvp;

import android.app.Activity;
import android.widget.RadioGroup;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;
import com.txtled.gp_a209.control.ControlActivity;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public interface ControlContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{
        void init(String endpoint, Activity activity);

        void sendMqtt(int id);
    }
}
