package com.txtled.gp_a209.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.AddDeviceActivity;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.control.ControlActivity;
import com.txtled.gp_a209.main.mvp.MainContract;
import com.txtled.gp_a209.main.mvp.MainPresenter;
import com.txtled.gp_a209.widget.ArialRoundButton;

import java.util.List;

import butterknife.BindView;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, DeviceListAdapter.OnDeviceClickListener {

    @BindView(R.id.rlv_device_list)
    RecyclerView rlvDeviceList;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.abt_off_all)
    ArialRoundButton abtOffAll;

    private DeviceListAdapter listAdapter;

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void init() {
        initToolbar();
        setNavigationIcon(false);
        tvTitle.setText(R.string.device_list);
        presenter.init();
        setRightImg(true, R.mipmap.devicelist_add_xhdpi, v ->
                startActivity(new Intent(MainActivity.this, AddDeviceActivity.class)));
        srlRefresh.setOnRefreshListener(this);
        rlvDeviceList.setHasFixedSize(true);
        rlvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new DeviceListAdapter(this,this);
        rlvDeviceList.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        srlRefresh.setRefreshing(false);
        if (!data.isEmpty())
            listAdapter.setData(data);
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
        startActivity(new Intent(this, ControlActivity.class).putExtra(ENDPOINT,endpoint).putExtra(NAME,name));
    }
}
