package com.txtled.gp_a209.main;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.main.mvp.MainContract;
import com.txtled.gp_a209.main.mvp.MainPresenter;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View {

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void init() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }



    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }
}
