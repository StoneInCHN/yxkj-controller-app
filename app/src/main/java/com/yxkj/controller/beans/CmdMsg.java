package com.yxkj.controller.beans;


import java.io.Serializable;

/**
 * 与后台通信的消息类
 *
 * @author huyong
 * @since 2017/9/26
 */
public class CmdMsg implements Serializable {

    private static final long serialVersionUID = -5098028238072646730L;
    private Long id;

    /**
     * 设备唯一编号
     */
    private String deviceNo;
    /**
     * 消息内容
     */
    private String content;

    /**
     * 货柜号
     */
    private int address;

    /**
     * 货柜类型
     * 1.弹簧柜
     * 2.格子柜
     */
    private int addressType;

    /**
     * 货道号
     */
    private int box;
    /**
     * 消息类型
     * 1.出货指令
     * 2.APP更新指令
     * 3.广告更新指令
     */
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "CmdMsg{" +
                "id=" + id +
                ", deviceNo='" + deviceNo + '\'' +
                ", content='" + content + '\'' +
                ", address=" + address +
                ", addressType=" + addressType +
                ", box=" + box +
                ", type=" + type +
                '}';
    }
}
