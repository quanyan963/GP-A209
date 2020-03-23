package com.txtled.gp_a209.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.AddDeviceActivity;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.control.ControlActivity;
import com.txtled.gp_a209.information.InfoActivity;
import com.txtled.gp_a209.main.mvp.MainContract;
import com.txtled.gp_a209.main.mvp.MainPresenter;
import com.txtled.gp_a209.widget.ArialRoundButton;

import java.util.List;

import butterknife.BindView;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;
import static com.txtled.gp_a209.utils.Constants.OK;
import static com.txtled.gp_a209.utils.Constants.RESULT;
import static com.txtled.gp_a209.utils.Constants.VERSION;
import static com.txtled.gp_a209.utils.Constants.WIFI;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, DeviceListAdapter.OnDeviceClickListener {

    @BindView(R.id.rlv_device_list)
    RecyclerView rlvDeviceList;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.abt_off_all)
    ArialRoundButton abtOffAll;

    private DeviceListAdapter listAdapter;
    private String userId;
    private String wifiName;

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void init() {
        initToolbar();
        setNavigationIcon(false);
        tvTitle.setText(R.string.device_list);
        userId = presenter.init(this);
        setRightImg(true, R.mipmap.devicelist_add_xhdpi, v ->
                startActivityForResult(new Intent(MainActivity.this,
                        AddDeviceActivity.class),RESULT));
        srlRefresh.setOnRefreshListener(this);
        rlvDeviceList.setHasFixedSize(true);
        rlvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new DeviceListAdapter(this,this,userId);
        rlvDeviceList.setAdapter(listAdapter);
        srlRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void getDeviceData(List<DeviceInfo> data) {
//        srlRefresh.setRefreshing(false);
//        if (!data.isEmpty())
//            listAdapter.setData(data);
    }

    @Override
    public void hidSnackBar() {
        hideSnackBar();
    }

    @Override
    public void checkLocation() {
        showSnackBar(abtOffAll, R.string.open_location);
    }

    @Override
    public void setNoWifiView() {
        showSnackBar(abtOffAll, R.string.no_wifi);
    }

    @Override
    public void closeRefresh() {
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void setData(List<WWADeviceInfo> refreshData) {
        srlRefresh.setRefreshing(false);
        if (!refreshData.isEmpty())
            listAdapter.setData(refreshData);
    }

    @Override
    public void noDevice() {
        srlRefresh.setRefreshing(false);
        showSnackBar(abtOffAll,R.string.no_device);
    }

    @Override
    public void getWifiName(String ssid) {
        wifiName = ssid;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return onExitActivity(keyCode,event);
    }

    /**
     * 列表点击事件
     * @param endpoint
     */
    @Override
    public void onDeviceClick(String endpoint,String name) {
        startActivity(new Intent(this, ControlActivity.class)
                .putExtra(ENDPOINT,endpoint).putExtra(NAME,name));
    }

    @Override
    public void onSettingClick(WWADeviceInfo data,String name) {
        startActivity(new Intent(this, InfoActivity.class).putExtra(NAME,name)
                .putExtra(ENDPOINT,data.getIp()).putExtra(VERSION,data.getVer())
                .putExtra(WIFI,wifiName));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT){
            if (resultCode == OK){
                srlRefresh.setRefreshing(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
