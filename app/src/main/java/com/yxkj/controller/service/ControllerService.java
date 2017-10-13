package com.yxkj.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EVPortRegisterResponse;
import com.yxkj.controller.service.handler.NettyClientBootstrap;
import com.yxkj.controller.share.SharePrefreceHelper;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;

import java.util.Map;

public class ControllerService extends Service {
    NettyClientBootstrap nettyStart = new NettyClientBootstrap();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放串口
        Map<Integer, String> addressMaps = MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressMap();
        for (int key : addressMaps.keySet()) {
            String response = EVprotocol.EVPortRelease(addressMaps.get(key));
            EVPortRegisterResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response, EVPortRegisterResponse.class);
            MyApplication.getMyApplication().getRegisterPort().remove(evPortRegisterResponse.getEV_json().getPort());
            LogUtil.d(response);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //从本地获取DeviceNo
        String imei = SharePrefreceHelper.getInstence(this).getDeviceNo("imei");
        if (imei == null || TextUtils.isEmpty(imei)) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            SharePrefreceHelper.getInstence(this).setDeviceNo("imei", imei);
        }
        MyApplication.getMyApplication().configBean.getDeviceInfo().setDeviceNo(imei);
        LogUtil.d("imei:" + imei);
        // TODO: 2017/9/29 register 串口

        //注册串口
        Map<Integer, String> addressMaps = MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressMap();

        for (int key : addressMaps.keySet()) {
            EVprotocol.EVPortRegister(addressMaps.get(key));
            String response = EVprotocol.EVPortRegister(addressMaps.get(key));
            EVPortRegisterResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response.replace("\\n", "").replace("\\t", ""), EVPortRegisterResponse.class);
            MyApplication.getMyApplication().getRegisterPort().put(evPortRegisterResponse.getEV_json().getPort(), evPortRegisterResponse.getEV_json().getPort_id());
            LogUtil.d(response);
        }
        new Thread(() -> {
            nettyStart.startNetty();
        }).start();
    }
}
