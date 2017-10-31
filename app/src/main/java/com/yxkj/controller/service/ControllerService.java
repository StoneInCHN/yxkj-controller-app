package com.yxkj.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EVJsonResponse;
import com.yxkj.controller.beans.MachineInfoRequest;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.service.handler.NettyClientBootstrap;
import com.yxkj.controller.tools.ControlAudioManager;
import com.yxkj.controller.util.DisplayUtil;
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
            EVJsonResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response, EVJsonResponse.class);
            if (evPortRegisterResponse.getEV_json().getPort_id() < 0) return;
            MyApplication.getMyApplication().getRegisterPort().remove(evPortRegisterResponse.getEV_json().getPort());
            LogUtil.d(response);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //从本地获取DeviceNo
        String imei = DisplayUtil.getImei();
        MyApplication.getMyApplication().configBean.getDeviceInfo().setDeviceNo(imei);
        LogUtil.d("imei:" + imei);
        //初始化参数
        MachineInfoRequest request = new MachineInfoRequest();
        request.setDeviceNo(imei);
        request.setVolume(String.valueOf(ControlAudioManager.newInstance().getCurrentStreamVolume()));
        HttpFactory.initMachineStatus(request);
        //注册串口
        List<String> serialPorts = MyApplication.getMyApplication().configBean.getDeviceInfo().getSerialPorts();

        for (String serialPost : serialPorts) {
            EVprotocol.EVPortRegister(serialPost);
            String response = EVprotocol.EVPortRegister(serialPost);
            EVJsonResponse evPortRegisterResponse = GsonUtil.getInstance().convertJsonStringToObject(response.replace("\\n", "").replace("\\t", ""), EVJsonResponse.class);
            if (evPortRegisterResponse.getEV_json().getPort_id() < 0) return;
            MyApplication.getMyApplication().getRegisterPort().put(evPortRegisterResponse.getEV_json().getPort(), evPortRegisterResponse.getEV_json().getPort_id());
            LogUtil.d(response);
        }
        new Thread(() -> {
            nettyStart.startNetty();
        }).start();
    }
}
