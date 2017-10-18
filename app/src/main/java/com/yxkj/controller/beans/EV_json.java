/**
 * Copyright 2017 bejson.com
 */
package com.yxkj.controller.beans;

/**
 * Auto-generated: 2017-09-30 17:30:40
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class EV_json {

    private int EV_type;
    /**
     * 串口号
     */
    private String port;
    /**
     * 端口号
     */
    private int port_id;
    /**
     * 货柜号
     */
    private int addr;
    /**
     * 货道号
     */
    private int box;
    /**
     * 出货成功或失败
     */
    private int is_success;
    /**
     * 出货返回值
     */
    private int result;

    public void setEV_type(int EV_type) {
        this.EV_type = EV_type;
    }

    public int getEV_type() {
        return EV_type;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setPort_id(int port_id) {
        this.port_id = port_id;
    }

    public int getPort_id() {
        return port_id;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getIs_success() {
        return is_success;
    }

    public void setIs_success(int is_success) {
        this.is_success = is_success;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "EV_json{" +
                "EV_type=" + EV_type +
                ", port='" + port + '\'' +
                ", port_id=" + port_id +
                ", addr=" + addr +
                ", box=" + box +
                ", is_success=" + is_success +
                ", result=" + result +
                '}';
    }
}