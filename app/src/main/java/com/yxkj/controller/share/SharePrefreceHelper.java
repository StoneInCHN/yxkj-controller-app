package com.yxkj.controller.share;

import android.content.Context;

import com.yxkj.controller.constant.Constant;

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

    /**
     * 设置顶部视频下载成功
     */
    public void setVideoTopDownloadSuceess(boolean value) {
        setBoolean(Constant.VIDEO_TOP, value);
    }

    /**
     * 获得顶部视频下载成功
     */
    public boolean getVideoTopDownloadSuceess() {
        return getBoolean(Constant.VIDEO_TOP, false);
    }

    /**
     * 设置顶部视频url
     */
    public void setVideoTopUrl(String url) {
        putString(Constant.VIDEO_TOP, url);
    }

    /**
     * 设置顶部视频url
     */
    public String getVideoTopUrl() {
        return getString(Constant.VIDEO_TOP);
    }

    /**
     * 设置底部视频下载成功
     */
    public void setVideoBottomDownloadSuceess(boolean value) {
        setBoolean(Constant.VIDEO_BOTTOM, value);
    }

    /**
     * 获得底部视频下载成功
     */
    public boolean getVideoBottomDownloadSuceess() {
        return getBoolean(Constant.VIDEO_BOTTOM, false);
    }

    /**
     * 设置底部视频url
     */
    public void setVideoBottomUrl(String url) {
        putString(Constant.VIDEO_BOTTOM, url);
    }

    /**
     * 设置底部视频url
     */
    public String getVideoBottomUrl() {
        return getString(Constant.VIDEO_BOTTOM);
    }


    /**
     * 设置DeviceId
     */
    public void setDeviceNo(String key, String value) {
        putString(key, value);
    }

    /**
     * 获得DeviceId
     *
     * @param key
     * @return
     */
    public String getDeviceNo(String key) {
        return getString(key);
    }
}
