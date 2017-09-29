package com.yxkj.controller.application;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 *
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MultiDex.install(this);
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
