package com.txtled.gp_a209.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.iot.esptouch.util.ByteUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.mvp.AddContract;
import com.txtled.gp_a209.add.mvp.AddPresenter;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundEditText;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public class AddDeviceActivity extends MvpBaseActivity<AddPresenter> implements AddContract.View,
        View.OnClickListener, TextWatcher {
    @BindView(R.id.aet_password)
    ArialRoundEditText aetPassword;
    @BindView(R.id.aet_wifi_name)
    ArialRoundEditText aetWifiName;
    @BindView(R.id.atv_no_pass)
    ArialRoundTextView atvNoPass;
    @BindView(R.id.abt_collect)
    ArialRoundButton abtCollect;
    @BindView(R.id.rlv_name_list)
    RecyclerView rlvNameList;
    @BindView(R.id.atv_top)
    ArialRoundTextView atvTop;
    @BindView(R.id.atv_eye_hint)
    ArialRoundTextView atvEyeHint;
    @BindView(R.id.atv_device_will)
    ArialRoundTextView atvDeviceWill;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.til_pass)
    TextInputLayout tilPass;

    private DeviceNameAdapter adapter;
    private String name;

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
            if (b) {
                presenter.configWifi(aetWifiName.getTag() == null ?
                        ByteUtil.getBytesByString(aetWifiName.getText().toString()) :
                        (byte[]) aetWifiName.getTag(), aetPassword.getText().toString());
            }
        });
        AlertUtils.showHintDialog(this, R.layout.alert_hint, getString(R.string.hint_title)
                , R.string.hint_msg, false);
        atvNoPass.setOnClickListener(this);
        aetPassword.addTextChangedListener(this);

        rlvNameList.setHasFixedSize(true);
        rlvNameList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceNameAdapter(this, name -> {
            presenter.setName(name);
            abtCollect.setEnabled(true);
        });
        rlvNameList.setAdapter(adapter);
        presenter.getConfiguredData();
    }

    private void changeBtnColor(boolean b) {
        abtCollect.setEnabled(b);
        if (b) {
            abtCollect.setBackgroundColor(getResources().getColor(R.color.black));
            abtCollect.setTextColor(getResources().getColor(R.color.yellow));
        } else {
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
        showSnackBar(abtCollect, R.string.open_location);
    }

    @Override
    public void setNoWifiView() {
        aetWifiName.setText(R.string.unknown);
        showSnackBar(abtCollect, R.string.no_wifi);
    }

    @Override
    public void setInfo(String ssid) {
        hideSnackBar();
        aetWifiName.setText(ssid);
    }

    @Override
    public void showConnectHint() {
        AlertUtils.showHintDialog(this, R.layout.alert_hint,
                getString(R.string.hint_device, aetWifiName.getText().toString()),
                R.string.hint_config_msg, true);
    }

    @Override
    public void configureSuccess(String address) {
        showList(true);
        presenter.connDevice(address);
        changeBtnColor(false);
    }

    @Override
    public void setData(String[] data) {
        adapter.setData(data);
    }

    private void showList(boolean b) {
        AlphaAnimation hid = new AlphaAnimation(1, 0);
        hid.setDuration(300);
        hid.setFillAfter(true);
        AlphaAnimation show = new AlphaAnimation(0, 1);
        show.setDuration(300);
        show.setFillAfter(true);
        hid.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (b) {
                    tilName.setVisibility(View.GONE);
                    tilPass.setVisibility(View.GONE);
                    atvEyeHint.setVisibility(View.GONE);
                    atvNoPass.setVisibility(View.GONE);
                    rlvNameList.setVisibility(View.VISIBLE);
                    rlvNameList.startAnimation(show);
                    atvTop.setText(R.string.connected);
                    atvDeviceWill.setText(R.string.new_name);
                    atvTop.startAnimation(show);
                    atvDeviceWill.startAnimation(show);
                } else {
                    rlvNameList.setVisibility(View.GONE);
                    tilName.setVisibility(View.VISIBLE);
                    tilPass.setVisibility(View.VISIBLE);
                    atvEyeHint.setVisibility(View.VISIBLE);
                    atvNoPass.setVisibility(View.VISIBLE);
                    tilName.startAnimation(show);
                    tilPass.startAnimation(show);
                    atvEyeHint.startAnimation(show);
                    atvNoPass.startAnimation(show);
                    atvTop.setText(R.string.log_wifi);
                    atvDeviceWill.setText(R.string.device_will_configured);
                    atvTop.startAnimation(show);
                    atvDeviceWill.startAnimation(show);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (b) {
            rlvNameList.setAlpha(0);
            tilName.startAnimation(hid);
            tilPass.startAnimation(hid);
            atvEyeHint.startAnimation(hid);
            atvNoPass.startAnimation(hid);
            atvTop.startAnimation(hid);
            atvDeviceWill.startAnimation(hid);
        } else {
            rlvNameList.startAnimation(hid);
            atvTop.startAnimation(hid);
            atvDeviceWill.startAnimation(hid);
        }
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 7) {
            if (aetPassword.getText().toString().trim().length() != 0) {
                changeBtnColor(true);
            } else {
                changeBtnColor(false);
            }
        } else {
            changeBtnColor(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (rlvNameList.getVisibility() == View.VISIBLE){
            showList(false);
        }else {
            super.onBackPressed();
        }
    }
}
