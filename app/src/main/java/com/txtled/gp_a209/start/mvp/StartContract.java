package com.txtled.gp_a209.start.mvp;

import android.app.Activity;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public interface StartContract {
    interface View extends BaseView{

        void toMain();

        void showPermissionHint();
    }
    interface Presenter extends BasePresenter<View>{

        void startTimer(Activity activity);

        boolean isLogin();
    }
}
