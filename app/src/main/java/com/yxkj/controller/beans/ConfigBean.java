package com.yxkj.controller.beans;

import java.util.Map;

/**
 * Created by huyong on 2017/9/29.
 */
public class ConfigBean {

    private String version;

    private DeviceInfo deviceInfo;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

}
