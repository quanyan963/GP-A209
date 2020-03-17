package com.txtled.gp_a209.add;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.mvp.AddContract;
import com.txtled.gp_a209.add.mvp.AddPresenter;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundEditText;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public class AddDeviceActivity extends MvpBaseActivity<AddPresenter> implements AddContract.View,
        View.OnClickListener {
    @BindView(R.id.aet_password)
    ArialRoundEditText aetPassword;
    @BindView(R.id.aet_wifi_name)
    ArialRoundEditText aetWifiName;
    @BindView(R.id.atv_no_pass)
    ArialRoundTextView atvNoPass;
    @BindView(R.id.abt_collect)
    ArialRoundButton abtCollect;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        setNavigationIcon(true);
        tvTitle.setText(R.string.add_device);
        changeBtnColor(false);
        aetPassword.requestFocus();
        abtCollect.setOnClickListener(this);
        presenter.init(this);
        AlertUtils.setListener(b -> {
            if (b){
                presenter.configWifi();
            }
        });
        AlertUtils.showHintDialog(this,R.layout.alert_hint,R.string.hint_title
                ,R.string.hint_msg,false,null,null);
        atvNoPass.setOnClickListener(this);
        aetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 7){
                    if (aetPassword.getText().toString().trim().length() != 0){
                        changeBtnColor(true);
                    }else {
                        changeBtnColor(false);
                    }
                }else {
                    changeBtnColor(false);
                }
            }
        });
    }

    private void changeBtnColor(boolean b) {
        abtCollect.setEnabled(b);
        if (b){
            abtCollect.setBackgroundColor(getResources().getColor(R.color.black));
            abtCollect.setTextColor(getResources().getColor(R.color.yellow));
        }else {
            abtCollect.setBackgroundColor(getResources().getColor(R.color.btn_un));
            abtCollect.setTextColor(getResources().getColor(R.color.text_un));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_device;
    }

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void hidSnackBar() {
        hideSnackBar();
    }

    @Override
    public void checkLocation() {
        aetWifiName.setText(R.string.unknown);
        showSnackBar(abtCollect,R.string.open_location);
    }

    @Override
    public void setNoWifiView() {
        aetWifiName.setText(R.string.unknown);
        showSnackBar(abtCollect,R.string.no_wifi);
    }

    @Override
    public void setInfo(String ssid, WifiInfo info) {
        hideSnackBar();
        aetWifiName.setText(ssid);
    }

    @Override
    public void showConnectHint() {
        AlertUtils.showHintDialog(this,R.layout.alert_hint,getString());
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId());
    }
}
