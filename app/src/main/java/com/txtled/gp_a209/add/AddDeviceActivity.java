package com.txtled.gp_a209.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espressif.iot.esptouch.util.ByteUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.listener.OnCreateThingListener;
import com.txtled.gp_a209.add.mvp.AddContract;
import com.txtled.gp_a209.add.mvp.AddPresenter;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundEditText;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;
import static com.txtled.gp_a209.utils.Constants.OK;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.TYPE;

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
    private AlertDialog dialog;
    private int type;//0:全流程 1：改名 2：改wifi
    private String ip;
    private String friendlyName;
    private String thing;
    private WWADeviceInfo info;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE,0);
        if (type == 1){
            ip = intent.getStringExtra(ENDPOINT);
            friendlyName = intent.getStringExtra(NAME);
            thing = intent.getStringExtra(THING_DIR);
            info = new WWADeviceInfo();
            info.setIp(ip);
            info.setThing(thing);
            info.setFriendlyNames(friendlyName);
            presenter.initData(info);
        }

        initToolbar();
        setNavigationIcon(true);
        changeBtnColor(false);
        aetPassword.requestFocus();
        abtCollect.setOnClickListener(this);
        presenter.init(this);
        aetWifiName.clearFocus();
        aetPassword.clearFocus();
        AlertUtils.setListener(b -> {
            if (b) {
                presenter.configWifi(aetWifiName.getTag() == null ?
                        ByteUtil.getBytesByString(aetWifiName.getText().toString()) :
                        (byte[]) aetWifiName.getTag(), aetPassword.getText().toString());
            }
        });
        if (type == 1){
            tvTitle.setText(R.string.change_name_big);
            dialog = AlertUtils.showLoadingDialog(this,R.layout.alert_progress);
            dialog.show();
            rlvNameList.setVisibility(View.VISIBLE);
            tilName.setVisibility(View.GONE);
            tilPass.setVisibility(View.GONE);
            atvNoPass.setVisibility(View.GONE);
            atvEyeHint.setVisibility(View.GONE);
            atvTop.setText(R.string.connected);
            atvDeviceWill.setText(R.string.new_name);
        }else if (type == 0){
            tvTitle.setText(R.string.add_device);
            AlertUtils.showHintDialog(this, R.layout.alert_hint, getString(R.string.hint_title)
                    , R.string.hint_msg, false);
        }else {
            tvTitle.setText(R.string.wifi_setup_big);
            AlertUtils.showHintDialog(this, R.layout.alert_hint, getString(R.string.hint_title)
                    , R.string.hint_msg, false);
        }
        atvNoPass.setOnClickListener(this);
        aetPassword.addTextChangedListener(this);

        rlvNameList.setHasFixedSize(true);
        rlvNameList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceNameAdapter(this, name -> {
            presenter.setName(name);
            changeBtnColor(true);
        });
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
    public void configureSuccess() {
        runOnUiThread(() -> {
            showList(true);
            changeBtnColor(false);
        });
    }

    /**
     * 给adapter添加数据
     * @param data dynamodb中得出的数据
     */
    @Override
    public void setData(String[] data) {
        adapter.setData(data);
        rlvNameList.setAdapter(adapter);
        if (type == 1){
            dialog.dismiss();
        }

    }

    /**
     * 过渡动画
     * @param b
     */
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
                    tilName.clearAnimation();
                    tilPass.clearAnimation();
                    atvEyeHint.clearAnimation();
                    atvNoPass.clearAnimation();
                    tilName.setVisibility(View.GONE);
                    tilPass.setVisibility(View.GONE);
                    atvEyeHint.setVisibility(View.GONE);
                    atvNoPass.setVisibility(View.GONE);
                    rlvNameList.setVisibility(View.VISIBLE);
                    rlvNameList.startAnimation(show);
                    //rlvNameList.setAlpha(1);
                    atvTop.setText(R.string.connected);
                    atvDeviceWill.setText(R.string.new_name);

                } else {
                    rlvNameList.clearAnimation();
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
                    changeBtnColor(true);
                }
                atvTop.startAnimation(show);
                atvDeviceWill.startAnimation(show);
                abtCollect.setAnimation(show);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        atvTop.startAnimation(hid);
        abtCollect.setAnimation(hid);
        atvDeviceWill.startAnimation(hid);
        if (b) {
            //rlvNameList.setAlpha(0);
            tilName.startAnimation(hid);
            tilPass.startAnimation(hid);
            atvEyeHint.startAnimation(hid);
            atvNoPass.startAnimation(hid);

        } else {
            rlvNameList.startAnimation(hid);
        }
    }

    /**
     * 所有的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId(),rlvNameList.getVisibility() == View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 密码判断
     * @param s password
     */
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
        if (rlvNameList.getVisibility() == View.VISIBLE && type == 0){
            showList(false);
        }else {
            super.onBackPressed();
            this.finish();
        }
    }

    //添加事务时界面更新
    @Override
    public void onStatueChange() {
        runOnUiThread(() -> dialog = AlertUtils.showProgressDialog(AddDeviceActivity.this));
    }

    @Override
    public void dismiss() {
        runOnUiThread(() -> {
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
                AlertUtils.showAlertDialog(this, R.string.success,
                        (dialog, which) -> {
                            this.setResult(OK);
                            this.finish();
                        });
                dialog = null;
            }
        });

    }

    @Override
    public void changeName(String name) {
        runOnUiThread(() -> {
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
                AlertUtils.showAlertDialog(this, R.string.rename_success,
                        (dialog, which) -> {
                            this.setResult(OK,new Intent().putExtra(NAME,name));
                            this.finish();
                        });
                dialog = null;
            }
        });
    }

    @Override
    public void showSuccess() {
        if (type == 2){
            AlertUtils.showAlertDialog(this, R.string.re_wifi,
                    (dialog, which) -> {
                        this.setResult(OK,new Intent().putExtra(NAME,
                                aetWifiName.getText().toString()));
                        this.finish();
                    });
        }else {
            AlertUtils.showAlertDialog(this, R.string.configure_result_success,
                    (dialog, which) -> configureSuccess());
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
