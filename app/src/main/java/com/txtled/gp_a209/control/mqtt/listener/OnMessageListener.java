package com.txtled.gp_a209.control.mqtt.listener;

public interface OnMessageListener {
    void onSuccess();
    void onFailure();
    void onTimeout();
}
