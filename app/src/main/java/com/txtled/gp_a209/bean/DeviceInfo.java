package com.txtled.gp_a209.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2020/3/16.
 */
public class DeviceInfo implements Serializable {
    private String deviceName;
    private String endpoint;

    public DeviceInfo(String deviceName, String endpoint) {
        this.deviceName = deviceName;
        this.endpoint = endpoint;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
