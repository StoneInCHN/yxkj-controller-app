package com.yxkj.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EVPortRegisterResponse;
import com.yxkj.controller.service.handler.NettyClientBootstrap;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;

import java.util.List;
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
        Map<String, Integer> registerPortMap = MyApplication.getMyApplication().getRegisterPort();
        for (String key : registerPortMap.keySet()) {
            String response = EVprotocol.EVPortRelease(key);
            EVPortRegisterResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response, EVPortRegisterResponse.class);
            if (evPortRegisterResponse.getEV_json().getPort_id() < 0) return;
            MyApplication.getMyApplication().getRegisterPort().remove(evPortRegisterResponse.getEV_json().getPort());
            LogUtil.d(response);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        MyApplication.getMyApplication().configBean.getDeviceInfo().setDeviceNo(imei);
        LogUtil.d("imei:" + imei);
        // TODO: 2017/9/29 register 串口

        //注册串口
        List<String> serialPorts = MyApplication.getMyApplication().configBean.getDeviceInfo().getSerialPorts();

        for (String serialPost : serialPorts) {
            EVprotocol.EVPortRegister(serialPost);
            String response = EVprotocol.EVPortRegister(serialPost);
            EVPortRegisterResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response.replace("\\n", "").replace("\\t", ""), EVPortRegisterResponse.class);
            if (evPortRegisterResponse.getEV_json().getPort_id() < 0) return;
            MyApplication.getMyApplication().getRegisterPort().put(evPortRegisterResponse.getEV_json().getPort(), evPortRegisterResponse.getEV_json().getPort_id());
            LogUtil.d(response);
        }
        new Thread(() -> {
            nettyStart.startNetty();
        }).start();
    }
}
