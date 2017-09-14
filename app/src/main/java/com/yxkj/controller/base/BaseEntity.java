package com.yxkj.controller.base;

/**
 * 服务器通用返回数据格式
 */
public class BaseEntity<E> {

    public int code;

    public String desc;

    public String token;

    public String page;

    public E msg;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", token='" + token + '\'' +
                ", page='" + page + '\'' +
                ", msg=" + msg +
                '}';
    }
}
