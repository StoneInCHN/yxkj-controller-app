package com.yxkj.controller.util;

import android.widget.Toast;

import com.yxkj.controller.application.MyApplication;


/**
 * Toast显示工具类
 */
public class ToastUtil {

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public static void showToast(String info) {
        Toast.makeText(MyApplication.getMyApplication(), info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public static void showToastLong(String info) {
        Toast.makeText(MyApplication.getMyApplication(), info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     */
    public static void showToast(int resId) {
        Toast.makeText(MyApplication.getMyApplication(), MyApplication.getMyApplication().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showToastLong(int resId) {
        Toast.makeText(MyApplication.getMyApplication(), resId, Toast.LENGTH_LONG).show();
    }

}
