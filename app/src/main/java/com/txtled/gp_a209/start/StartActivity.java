package com.txtled.gp_a209.start;

import android.content.Intent;
import android.view.WindowManager;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.login.LoginActivity;
import com.txtled.gp_a209.start.mvp.StartContract;
import com.txtled.gp_a209.start.mvp.StartPresenter;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public class StartActivity extends MvpBaseActivity<StartPresenter> implements StartContract.View {
    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void beforeContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void init() {
        presenter.startTimer(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void toMain() {
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

    @Override
    public void showPermissionHint() {
        this.finish();
    }
}
