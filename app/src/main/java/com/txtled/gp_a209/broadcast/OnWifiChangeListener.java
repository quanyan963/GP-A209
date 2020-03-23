package com.txtled.gp_a209.broadcast;

import android.content.Context;
import android.net.wifi.WifiInfo;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public interface OnWifiChangeListener {
    void onWifiChanged(Context context, WifiInfo info);
}
