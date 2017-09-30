package com.yxkj.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.service.handler.NettyClientBootstrap;
import com.yxkj.controller.util.LogUtil;

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
        String json = EVprotocol.EVPortRelease("/dev/ttyS1");
        System.out.println(json);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: 2017/9/29 register 串口
        String json = EVprotocol.EVPortRelease("/dev/ttyS1");
        LogUtil.d(json);
        String json1 = EVprotocol.EVPortRegister("/dev/ttyS1");
        LogUtil.d(json1);
        new Thread(() -> {
            try {
                nettyStart.startNetty();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
