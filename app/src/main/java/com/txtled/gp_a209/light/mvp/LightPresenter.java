package com.txtled.gp_a209.light.mvp;

import android.app.Activity;

import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2020/5/25.
 */
public class LightPresenter extends RxPresenter<LightContract.View> implements LightContract.Presenter {
    private DataManagerModel dataManagerModel;
    @Inject
    public LightPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }


}
