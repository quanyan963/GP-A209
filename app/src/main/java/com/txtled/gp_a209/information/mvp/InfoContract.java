package com.txtled.gp_a209.information.mvp;

import android.app.Activity;
import android.view.View;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public interface InfoContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

        void init(Activity activity);

        String getName(String friendlyName);
    }
}
