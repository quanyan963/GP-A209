package com.txtled.gp_a209.control;

import android.content.Intent;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.control.mvp.ControlContract;
import com.txtled.gp_a209.control.mvp.ControlPresenter;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public class ControlActivity extends MvpBaseActivity<ControlPresenter> implements ControlContract.View {
    private String name;
    private String endpoint;
    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        endpoint = intent.getStringExtra(ENDPOINT);
        tvTitle.setText(name);
        setNavigationIcon(true);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_control;
    }

    @Override
    protected void beforeContentView() {

    }
}
