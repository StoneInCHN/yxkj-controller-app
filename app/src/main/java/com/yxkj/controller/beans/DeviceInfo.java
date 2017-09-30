package com.yxkj.controller.beans;

import java.util.Map;

/**
 * Created by huyong on 2017/9/30.
 */
public class DeviceInfo {
    private String deviceNo;
    private Map<Integer, Integer> addressMap;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Map<Integer, Integer> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map<Integer, Integer> addressMap) {
        this.addressMap = addressMap;
    }
}
