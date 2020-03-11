package com.txtled.gp_a209.start.mvp;

import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.utils.RxUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public class StartPresenter extends RxPresenter<StartContract.View> implements StartContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public StartPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void startTimer() {
        addSubscribe(Flowable.timer(4, TimeUnit.SECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Long>(view){

                    @Override
                    public void onNext(Long aLong) {
                        view.toMain();
                    }
                }));
    }
}
