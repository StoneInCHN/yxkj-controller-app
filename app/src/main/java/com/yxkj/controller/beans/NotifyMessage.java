package com.yxkj.controller.beans;

import java.io.Serializable;

/**
 * 与中控通信的消息类
 *
 * @author huyong
 * @since 2017/9/23
 */
public class NotifyMessage implements Serializable {

    public enum MsgType {
        /**
         * 注册设备
         */
        REGISTER,
        /**
         * 提示消息
         */
        NOTIFY
    }

    /**
     * 消息内容
     */
    private String content;

    private MsgType msgType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }
}
