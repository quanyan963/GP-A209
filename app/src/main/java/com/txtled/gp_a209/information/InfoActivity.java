package com.txtled.gp_a209.information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.AddDeviceActivity;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.information.mvp.InfoContract;
import com.txtled.gp_a209.information.mvp.InfoPresenter;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.MyView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;
import static com.txtled.gp_a209.utils.Constants.TYPE;
import static com.txtled.gp_a209.utils.Constants.VERSION;
import static com.txtled.gp_a209.utils.Constants.WIFI;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public class InfoActivity extends MvpBaseActivity<InfoPresenter> implements InfoContract.View {
    @BindView(R.id.mv_change_name)
    MyView mvChangeName;
    @BindView(R.id.mv_version)
    MyView mvVersion;
    @BindView(R.id.mv_wifi)
    MyView mvWifi;

    private String name;
    private String ip;
    private String version;
    private String wifiName;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        tvTitle.setText(R.string.information);
        setNavigationIcon(true);
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        ip = intent.getStringExtra(ENDPOINT);
        version = intent.getStringExtra(VERSION);
        wifiName = intent.getStringExtra(WIFI);
        mvChangeName.setRightText(name);
        mvVersion.setRightText(version);
        mvWifi.setRightText(wifiName);
        presenter.init(this);
        mvChangeName.setListener(v -> startActivity(new Intent(InfoActivity.this,
                AddDeviceActivity.class).putExtra(TYPE,1).putExtra(ENDPOINT,ip)));
        mvVersion.setListener(v -> {});
    }

    @Override
    public int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void beforeContentView() {

    }
}
