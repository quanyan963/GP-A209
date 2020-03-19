package com.txtled.gp_a209.control.mvp;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public interface ControlContract {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

    }
}
