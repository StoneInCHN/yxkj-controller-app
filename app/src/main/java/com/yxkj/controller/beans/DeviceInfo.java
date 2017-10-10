package com.yxkj.controller.beans;

import java.util.Map;

/**
 * Created by huyong on 2017/9/30.
 */
public class DeviceInfo {
    private String deviceNo;

    private Map<Integer, Integer> boxMap;

    private Map<Integer, String> addressMap;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Map<Integer, String> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map<Integer, String> addressMap) {
        this.addressMap = addressMap;
    }

    public Map<Integer, Integer> getBoxMap() {
        return boxMap;
    }

    public void setBoxMap(Map<Integer, Integer> boxMap) {
        this.boxMap = boxMap;
    }
}
