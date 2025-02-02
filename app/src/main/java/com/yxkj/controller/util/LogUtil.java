package com.yxkj.controller.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.yxkj.controller.BuildConfig;

import java.util.Locale;

/**
 * 一个日志的工具类 可以开启和关闭打印日志 最好不要用System打印，消耗内存。
 */
public class LogUtil {

    private static boolean isLogEnabled = BuildConfig.DEBUG;// 默认开启
    private static final String defaultTag = "sincere";// log默认的 tag
    private static final String TAG_CONTENT_PRINT = "%s.%s:%d";

    /**
     * 获得当前的 堆栈
     *
     * @return
     */
    private static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];

    }

    /**
     * 设置 debug是否启用 根据 判断 是否 为上线模式 android:debuggable
     * 打包后变为false，没打包前为true
     * <p/>
     * 要在application中 首先进行调用此方法 对 isLogEnabled 进行赋值
     *
     * @param context
     * @return
     */
    public static void setDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            isLogEnabled = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            // 友盟上报错误日志
            // MobclickAgent.reportError(context, e);
        }
    }

    /**
     * 获取是否DEBUG模式
     *
     * @return
     */
    public static boolean isDebugable() {
        return isLogEnabled;
    }

    /**
     * 打印的log信息 类名.方法名:行数--->msg
     *
     * @param trace
     * @return
     */
    private static String getContent(StackTraceElement trace) {
        return String.format(Locale.CHINA, TAG_CONTENT_PRINT, trace.getClassName(), trace.getMethodName(),
                trace.getLineNumber());
    }

    /**
     * debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (isLogEnabled) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg, tr);
        }
    }

    /**
     * debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isLogEnabled) {
            // getContent(getCurrentStackTraceElement())
            Log.d(tag, "--->" + msg);
        }
    }

    /**
     * debug
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isLogEnabled) {
            Log.d(defaultTag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * error
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (isLogEnabled) {
            Log.e(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg, tr);
        }
    }

    /**
     * error
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isLogEnabled) {
            Log.e(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * error
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isLogEnabled) {
            Log.e(defaultTag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * info
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (isLogEnabled) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg, tr);
        }
    }

    /**
     * info
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isLogEnabled) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * info
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isLogEnabled) {
            Log.i(defaultTag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * verbose
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (isLogEnabled) {
            Log.v(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg, tr);
        }
    }

    /**
     * verbose
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isLogEnabled) {
            Log.v(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * verbose
     *
     * @param msg
     */
    public static void v(String msg) {
        if (isLogEnabled) {
            Log.v(defaultTag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * warn
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (isLogEnabled) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg, tr);
        }
    }

    /**
     * warn
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (isLogEnabled) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }

    /**
     * warn
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isLogEnabled) {
            Log.w(defaultTag, getContent(getCurrentStackTraceElement()) + "--->" + msg);
        }
    }
}
