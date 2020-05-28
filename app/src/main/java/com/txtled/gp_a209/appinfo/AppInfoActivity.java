package com.txtled.gp_a209.appinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.appinfo.mvp.AppInfoConteact;
import com.txtled.gp_a209.appinfo.mvp.AppInfoPresenter;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.login.LoginActivity;
import com.txtled.gp_a209.widget.MyView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.txtled.gp_a209.utils.Constants.LOGIN;
import static com.txtled.gp_a209.utils.Constants.OK;
import static com.txtled.gp_a209.utils.Constants.TYPE;

/**
 * Created by Mr.Quan on 2020/3/24.
 */
public class AppInfoActivity extends MvpBaseActivity<AppInfoPresenter> implements AppInfoConteact.View {
    @BindView(R.id.mv_booklet)
    MyView mvBooklet;
    @BindView(R.id.mv_about)
    MyView mvAbout;
    @BindView(R.id.mv_skill)
    MyView mvSkill;
    @BindView(R.id.mv_help)
    MyView mvHelp;
    @BindView(R.id.mv_version)
    MyView mvVersion;
    @BindView(R.id.mv_account)
    MyView mvAccount;

    private String email;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        tvTitle.setText(R.string.information);
        setNavigationIcon(true);
        setEmailText();
        try {
            mvVersion.setRightText(getPackageManager()
                    .getPackageInfo(getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //退出登录
        mvAccount.setListener(v ->
                startActivityForResult(new Intent(AppInfoActivity.this,LoginActivity.class)
                        .putExtra(TYPE,1),LOGIN));
    }

    private void setEmailText() {
        email = presenter.geEmail();
        mvAccount.setRightText(email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LOGIN){
            if (resultCode == OK){
                setEmailText();
                this.setResult(OK);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_app_info;
    }

    @Override
    protected void beforeContentView() {

    }
}
