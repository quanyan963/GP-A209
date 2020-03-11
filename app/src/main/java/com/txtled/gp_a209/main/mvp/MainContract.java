package com.txtled.gp_a209.main.mvp;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public interface MainContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
