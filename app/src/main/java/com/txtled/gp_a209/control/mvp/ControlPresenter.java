package com.txtled.gp_a209.control.mvp;

import android.app.Activity;
import android.widget.RadioGroup;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public class ControlPresenter extends RxPresenter<ControlContract.View> implements ControlContract.Presenter {
    private DataManagerModel dataManagerModel;
    private int lineOne;
    private int lineTwo;

    @Inject
    public ControlPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public void init(String endpoint, Activity activity) {

    }

    @Override
    public void sendMqtt(int id) {
        switch (id){
            case
        }
    }
}
