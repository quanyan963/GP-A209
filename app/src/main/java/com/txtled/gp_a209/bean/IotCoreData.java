package com.txtled.gp_a209.bean;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2020/3/20.
 */
public class IotCoreData implements Serializable {
    private int light;
    private int sound;
    private int duration;
    private int volume;
    private String device;

    public IotCoreData(int light, int sound, int duration, int volume, String device) {
        this.light = light;
        this.sound = sound;
        this.duration = duration;
        this.volume = volume;
        this.device = device;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
