package com.yxkj.controller.share;

import android.content.Context;

import com.yxkj.controller.util.ACache;

/**
 *
 */

public class PrefrenceWrapper {
    private ACache sharedPreferences;

    private static final String SP_NAME = "share_controller";

    protected PrefrenceWrapper(Context context) {
        sharedPreferences = ACache.get(context);//Context.MODE_MULTI_PROCESS支持跨进程访问
    }

    protected void putString(String key, String value) {
        sharedPreferences.put(key, value);
    }

    protected String getString(String key) {
        String value = sharedPreferences.getAsString(key);
        return value == null ? "" : value;
    }

    protected void setBoolean(String key, boolean vaule) {
        sharedPreferences.put(key, String.valueOf(vaule));
    }

    protected Boolean getBoolean(String key) {
        String value = sharedPreferences.getAsString(key);
        return value == null ? false : Boolean.valueOf(value);
    }

    protected void setLong(String key, long value) {
        sharedPreferences.put(key, value + "");
    }

    protected long getLong(String key) {
        String value = sharedPreferences.getAsString(key);
        return value == null ? 0 : Long.valueOf(value);
    }

}
