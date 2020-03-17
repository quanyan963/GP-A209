package com.txtled.gp_a209.add.mvp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.core.location.LocationManagerCompat;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public class AddPresenter extends RxPresenter<AddContract.View> implements AddContract.Presenter {
    private DataManagerModel dataManagerModel;

    @Inject
    public AddPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public void init(Activity activity) {
        registerBroadcast(activity);
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.abt_collect:
            case R.id.atv_no_pass:
                view.showConnectHint();
                break;
        }
    }

    @Override
    public void configWifi() {

    }

    public boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(context,wifiManager.getConnectionInfo());
                    break;
            }
        }
    };

    private void onWifiChanged(Context context, WifiInfo info) {
        view.hidSnackBar();
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {

            if (isSDKAtLeastP()) {
                LocationManager locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);

                if (!(locationManager != null && LocationManagerCompat
                        .isLocationEnabled(locationManager))){
                    view.checkLocation();
                }else {
                    view.setNoWifiView();
                }
            }else {
                view.setNoWifiView();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            view.setInfo(ssid, info);
        }
    }

    private void registerBroadcast(Activity activity) {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        activity.registerReceiver(mReceiver, filter);
    }
}
