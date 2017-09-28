package com.yxkj.controller.share;

import android.content.Context;

/**
 *
 */

public class SharePrefreceHelper extends PrefrenceWrapper {

    private static SharePrefreceHelper sharePrefreceHelper;

    private SharePrefreceHelper(Context context) {
        super(context);
    }

    public static SharePrefreceHelper getInstence(Context context) {
        if (sharePrefreceHelper == null)
            sharePrefreceHelper = new SharePrefreceHelper(context);
        return sharePrefreceHelper;
    }

    public void setFirstBoolean(String key, boolean vaule) {
        setBoolean("first", vaule);
    }

    public boolean getFirstBoolean(String key, boolean defaultVaule) {
        return getBoolean(key, defaultVaule);
    }
}
