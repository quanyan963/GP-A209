package com.txtled.gp_a209.control.mqtt.listener;

import com.amazonaws.services.iot.client.AWSIotMessage;

public interface OnSuccessListener {
    void onSuccess();
    void onFailure();
    void onTimeout();
    void onMessage(AWSIotMessage message);
}
