package com.txtled.gp_a209.control.mqtt.listener;

import com.amazonaws.services.iot.client.AWSIotDevice;

/**
 * Created by Mr.Quan on 2020/3/20.
 */
public interface OnConnectListener {
    void onSuccess(AWSIotDevice device);

    void onFail();

}
