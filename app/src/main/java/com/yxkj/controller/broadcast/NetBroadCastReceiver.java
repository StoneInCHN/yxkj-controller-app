package com.yxkj.controller.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yxkj.controller.callback.NetStateChangeCallback;
import com.yxkj.controller.util.ToastUtil;

/**
 * 网络变化广播接收者
 */
public class NetBroadCastReceiver extends BroadcastReceiver {
    private NetStateChangeCallback netStateChangeCallback;
    private boolean isConnect;

    public void setNetStateChangeCallback(NetStateChangeCallback netStateChangeCallback) {
        this.netStateChangeCallback = netStateChangeCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取网络连接管理器
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                //获取网络连接信息
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                            || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        if (!isConnect) {
                            if (netStateChangeCallback != null) {
                                isConnect = true;
                                netStateChangeCallback.onConnected();
                            }
                        }
                    } else {
                        ToastUtil.showToast("网络已断开");
                        if (isConnect) {
                            if (netStateChangeCallback != null) {
                                isConnect = false;
                                netStateChangeCallback.onDisconnected();
                            }
                        }
                    }
                } else {
                    ToastUtil.showToast("网络已断开");
                    if (!isConnect) {
                        isConnect = false;
                    }
                }
            }
        }
    }
}