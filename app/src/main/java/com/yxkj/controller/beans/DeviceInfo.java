package com.yxkj.controller.beans;

import java.util.List;
import java.util.Map;

/**
 * Created by huyong on 2017/9/30.
 */
public class DeviceInfo {
    private String deviceNo;

    private Map<Integer, Integer> boxMap;

    private List<String> serialPorts;

    private Map<String, Integer> addressPhysicMap;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Map<Integer, Integer> getBoxMap() {
        return boxMap;
    }

    public void setBoxMap(Map<Integer, Integer> boxMap) {
        this.boxMap = boxMap;
    }

    public Map<String, Integer> getAddressPhysicMap() {
        return addressPhysicMap;
    }

    public void setAddressPhysicMap(Map<String, Integer> addressPhysicMap) {
        this.addressPhysicMap = addressPhysicMap;
    }

    public List<String> getSerialPorts() {
        return serialPorts;
    }

    public void setSerialPorts(List<String> serialPorts) {
        this.serialPorts = serialPorts;
    }
}
