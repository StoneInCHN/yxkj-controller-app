package com.yxkj.controller.callback;

/**
 * 输入结束监听
 */
public interface InputEndListener<T> {
    void onEnd(T param);
}