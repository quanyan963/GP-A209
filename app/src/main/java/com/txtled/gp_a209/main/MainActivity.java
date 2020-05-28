package com.txtled.gp_a209.main;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.AddDeviceActivity;
import com.txtled.gp_a209.appinfo.AppInfoActivity;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.control.ControlActivity;
import com.txtled.gp_a209.information.InfoActivity;
import com.txtled.gp_a209.main.mvp.MainContract;
import com.txtled.gp_a209.main.mvp.MainPresenter;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;

import java.util.List;

import butterknife.BindView;

import static com.txtled.gp_a209.utils.Constants.APP;
import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.INFO;
import static com.txtled.gp_a209.utils.Constants.NAME;
import static com.txtled.gp_a209.utils.Constants.OK;
import static com.txtled.gp_a209.utils.Constants.RESULT;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.TYPE;
import static com.txtled.gp_a209.utils.Constants.VERSION;
import static com.txtled.gp_a209.utils.Constants.WIFI;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, DeviceListAdapter.OnDeviceClickListener,
        View.OnClickListener {

    @BindView(R.id.rlv_device_list)
    RecyclerView rlvDeviceList;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.abt_off_all)
    ArialRoundButton abtOffAll;

    private DeviceListAdapter listAdapter;
    private String userId;
    private String wifiName;
    private AlertDialog dialog;
    private String name;

    @Override
    protected void beforeContentView() {

    }

    @Override
    public void init() {
        initToolbar();
        setNavigationIcon(false);
        tvTitle.setText(R.string.device_list);
        userId = presenter.init(this);
        abtOffAll.setOnClickListener(this);
        setRightImg(true, R.mipmap.devicelist_add_xhdpi, v ->
                startActivityForResult(new Intent(MainActivity.this,
                        AddDeviceActivity.class).putExtra(TYPE,0), RESULT));
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
    public void getDeviceData(List<WWADeviceInfo> data) {
        srlRefresh.setRefreshing(false);
        if (!data.isEmpty())
            listAdapter.setData(data);
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
        //srlRefresh.setRefreshing(false);

        if (!refreshData.isEmpty()){
            hidSnackBar();
            listAdapter.setDiscoveryData(refreshData);
        }

    }

    @Override
    public void noDevice() {
        //srlRefresh.setRefreshing(false);
        hidSnackBar();
        //showSnackBar(abtOffAll,R.string.no_device);
        showSnackBar(abtOffAll,R.string.no_device, R.string.retry, v -> {
            presenter.discovery();
            hidSnackBar();
            showSnackBar(abtOffAll,R.string.searching_device);
        });
    }

    @Override
    public void getWifiName(String ssid) {
        wifiName = ssid;
    }

    @Override
    public void deleteError() {
        dialog.dismiss();
        hidSnackBar();
        showSnackBar(abtOffAll,R.string.already_deleted);
    }

    @Override
    public void deleteSuccess() {
        dialog.dismiss();
        listAdapter.deleteItem(name);
    }

    @Override
    public void mqttInitFail() {
        dialog.dismiss();
        hidSnackBar();
        showSnackBar(abtOffAll,R.string.try_again);
    }

    @Override
    public void success(boolean allOff) {
        dialog.dismiss();
        if (allOff){
            //on
            abtOffAll.setBackgroundColor(getResources().getColor(R.color.black));
            abtOffAll.setTextColor(getResources().getColor(R.color.yellow));
            abtOffAll.setText(R.string.on_all);
        }else {
            //off
            abtOffAll.setBackgroundColor(getResources().getColor(R.color.gray));
            abtOffAll.setTextColor(getResources().getColor(R.color.bg_snack));
            abtOffAll.setText(R.string.off_all);
        }
    }

    @Override
    public void fail() {
        dialog.dismiss();
        hidSnackBar();
        showSnackBar(abtOffAll,R.string.some_no_responding);
    }

    @Override
    public void showLoading() {
        dialog = AlertUtils.showLoadingDialog(this,R.layout.alert_progress);
        dialog.show();
    }

    @Override
    public void showSearching() {
        runOnUiThread(() -> {
            hidSnackBar();
            showSnackBar(abtOffAll, R.string.searching_device);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (snackbar != null && snackbar.isShown()){
            hidSnackBar();
            return false;
        }else {
            return onExitActivity(keyCode,event);
        }
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
        startActivityForResult(new Intent(this, InfoActivity.class)
                .putExtra(NAME, data.getFriendlyNames())
                .putExtra(ENDPOINT,data.getIp()).putExtra(VERSION,data.getVer())
                .putExtra(WIFI,wifiName).putExtra(THING_DIR,data.getThing()),INFO);
    }

    /**
     * 侧滑删除按钮
     * @param data
     * @param name
     */
    @Override
    public void onDeleteClick(WWADeviceInfo data,String name) {
        this.name = name;
        dialog = AlertUtils.showLoadingDialog(this,R.layout.alert_progress);
        dialog.show();
        presenter.deleteDevice(data,name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT || requestCode == INFO || requestCode == APP){
            if (resultCode == OK){
                srlRefresh.setRefreshing(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLeftClick() {
        startActivityForResult(new Intent(this, AppInfoActivity.class),APP);
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
