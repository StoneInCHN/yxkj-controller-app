package com.yxkj.controller.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.yxkj.controller.beans.ConfigBean;
import com.yxkj.controller.share.Configure;

/**
 *
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public static ConfigBean configBean;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MultiDex.install(this);
        Configure configure = new Configure(this);
        configure.saveConfig();

    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
