package com.txtled.gp_a209.main.mvp;

import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }
}
