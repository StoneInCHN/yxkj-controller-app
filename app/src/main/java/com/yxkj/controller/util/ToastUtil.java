package com.yxkj.controller.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yxkj.controller.R;
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
        LayoutInflater inflater = LayoutInflater
                .from(MyApplication.getMyApplication());
        View view = inflater.inflate(R.layout.view_toast, null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(info);
        Toast toast = Toast.makeText(MyApplication.getMyApplication(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
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
