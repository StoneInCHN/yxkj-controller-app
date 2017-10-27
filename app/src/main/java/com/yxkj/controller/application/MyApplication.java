package com.yxkj.controller.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.yxkj.controller.BuildConfig;
import com.yxkj.controller.beans.ConfigBean;
import com.yxkj.controller.share.Configure;
import com.yxkj.controller.tools.CrashHandler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public ConfigBean configBean;

    private Map<String, Integer> registerPort;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MultiDex.install(this);
        if (!BuildConfig.DEBUG) {
            CrashHandler.getInstance().init(this);
        }
        Configure configure = new Configure(this);
        configure.saveConfig();
        registerPort = new HashMap<>();
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public Map<String, Integer> getRegisterPort() {
        return registerPort;
    }

    public void setRegisterPort(Map<String, Integer> registerPort) {
        this.registerPort = registerPort;
    }
}
