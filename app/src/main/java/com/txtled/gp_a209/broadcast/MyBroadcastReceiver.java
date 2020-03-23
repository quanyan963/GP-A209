package com.txtled.gp_a209.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private OnWifiChangeListener listener;
    public MyBroadcastReceiver(OnWifiChangeListener listener) {
        this.listener = listener;
    }

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
                listener.onWifiChanged(context,wifiManager.getConnectionInfo());
                break;
        }
    }
}
