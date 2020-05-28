package com.txtled.gp_a209.light.mvp;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/5/25.
 */
public interface LightContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
