package com.txtled.gp_a209.appinfo.mvp;

import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2020/3/24.
 */
public class AppInfoPresenter extends RxPresenter<AppInfoConteact.View> implements AppInfoConteact.Presenter {
    private DataManagerModel dataManagerModel;

    @Inject
    public AppInfoPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public String geEmail() {
        return dataManagerModel.getEmail();
    }
}
