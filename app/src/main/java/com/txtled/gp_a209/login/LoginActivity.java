package com.txtled.gp_a209.login;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.login.mvp.LoginContract;
import com.txtled.gp_a209.login.mvp.LoginPresenter;
import com.txtled.gp_a209.main.MainActivity;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2020/3/12.
 */
public class LoginActivity extends MvpBaseActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {
    @BindView(R.id.atv_problem)
    ArialRoundTextView atvProblem;
    @BindView(R.id.abt_login)
    ArialRoundButton abtLogin;
    @BindView(R.id.atv_wifi_name)
    ArialRoundTextView atvWifiName;

    private AlertDialog dialog;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        presenter.init(this);
        atvProblem.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getResources()
                .getText(R.string.problems));
        stringBuilder.setSpan(new FirstClick(),27,getResources().getString(R.string.problems)
                .length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        atvProblem.setMovementMethod(LinkMovementMethod.getInstance());
        atvProblem.setText(stringBuilder);

        abtLogin.setOnClickListener(this);
        dialog = AlertUtils.showLoadingDialog(this,R.layout.alert_progress);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void onClick(View v) {
        presenter.viewClick(v.getId());
    }

    @Override
    public void hidLoadingView() {
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void showLoadingView() {
        dialog.show();
    }

    @Override
    public void checkLocation() {
        atvWifiName.setText(R.string.unknown);
        showSnackBar(atvProblem,R.string.open_location);
    }

    @Override
    public void setNoWifiView() {
        atvWifiName.setText(R.string.unknown);
        showSnackBar(atvProblem,R.string.no_wifi);
    }

    @Override
    public void hidSnackBar() {
        hideSnackBar();
    }

    @Override
    public void setInfo(String ssid, WifiInfo info) {
        atvWifiName.setText(ssid);
    }

    @Override
    public void showLoginFail() {
        hidLoadingView();
        hideSnackBar();
        showSnackBar(atvProblem,R.string.login_failed);
    }

    @Override
    public void toMainView() {
        hidLoadingView();
        startActivity(new Intent(this, MainActivity.class));
    }

    private class FirstClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            showSnackBar(atvProblem,R.string.maintenance);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.yellow));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume(dialog.isShowing());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        if (snackbar != null && snackbar.isShown()){
            snackbar.dismiss();
            snackbar = null;
        }else {
            super.onBackPressed();
        }
    }
}
