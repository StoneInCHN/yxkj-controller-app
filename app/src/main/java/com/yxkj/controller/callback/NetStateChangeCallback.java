package com.yxkj.controller.callback;

/**
 * 网络变化
 */

public interface NetStateChangeCallback {
    void onConnected();

    void onDisconnected();
}
