package com.txtled.gp_a209.appinfo.mvp;

import com.txtled.gp_a209.base.BasePresenter;
import com.txtled.gp_a209.base.BaseView;

/**
 * Created by Mr.Quan on 2020/3/24.
 */
public interface AppInfoConteact {
    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

        String geEmail();
    }
}
