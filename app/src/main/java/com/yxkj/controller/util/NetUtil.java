package com.yxkj.controller.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetUtil
 */
public class NetUtil {
    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable();
    }
}
