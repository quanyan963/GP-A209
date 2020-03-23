package com.txtled.gp_a209.information;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.AddDeviceActivity;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.information.mvp.InfoContract;
import com.txtled.gp_a209.information.mvp.InfoPresenter;
import com.txtled.gp_a209.widget.MyView;

import butterknife.BindView;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME_RESULT;
import static com.txtled.gp_a209.utils.Constants.NAME;
import static com.txtled.gp_a209.utils.Constants.OK;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.TYPE;
import static com.txtled.gp_a209.utils.Constants.VERSION;
import static com.txtled.gp_a209.utils.Constants.WIFI;
import static com.txtled.gp_a209.utils.Constants.WIFI_RESULT;

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

    private String friendlyName;
    private String ip;
    private String version;
    private String wifiName;
    private String thing;
    private boolean hasChanged = false;

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
        friendlyName = intent.getStringExtra(NAME);
        ip = intent.getStringExtra(ENDPOINT);
        version = intent.getStringExtra(VERSION);
        wifiName = intent.getStringExtra(WIFI);
        thing = intent.getStringExtra(THING_DIR);
        presenter.init(this);
        mvChangeName.setRightText(presenter.getName(friendlyName));
        mvVersion.setRightText(version);
        mvWifi.setRightText(wifiName);

        mvChangeName.setListener(v -> startActivityForResult(new Intent(InfoActivity.this,
                AddDeviceActivity.class).putExtra(TYPE,1).putExtra(ENDPOINT,ip)
                .putExtra(THING_DIR,thing).putExtra(NAME,friendlyName), NAME_RESULT));
        mvVersion.setListener(v -> {});
        mvWifi.setListener(v -> startActivityForResult(new Intent(InfoActivity.this,
                AddDeviceActivity.class).putExtra(TYPE,2),WIFI_RESULT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NAME_RESULT){
            if (requestCode == OK){
                hasChanged = true;
                mvChangeName.setRightText(data.getStringExtra(NAME));
            }
        }else if (requestCode == WIFI_RESULT){
            hasChanged = true;
            mvWifi.setRightText(data.getStringExtra(NAME));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void onBackPressed() {
        if (hasChanged){
            this.setResult(OK);
            hasChanged = false;

        }
        super.onBackPressed();
    }
}
