package com.txtled.gp_a209.control.mqtt.listener;

public interface OnSuccessListener {
    void onSuccess();
    void onFailure();
    void onTimeout();
    void onMessage();
}
