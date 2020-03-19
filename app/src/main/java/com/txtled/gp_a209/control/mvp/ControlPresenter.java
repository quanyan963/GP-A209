package com.txtled.gp_a209.control.mvp;

import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public class ControlPresenter extends RxPresenter<ControlContract.View> implements ControlContract.Presenter {
    private DataManagerModel dataManagerModel;

    @Inject
    public ControlPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

}
