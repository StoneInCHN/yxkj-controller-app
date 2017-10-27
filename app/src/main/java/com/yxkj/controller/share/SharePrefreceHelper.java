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
     * 设置顶部视频下载进度
     */
    public void setVideoTopDownloaded(long videoTopDownloaded) {
        setLong(Constant.VIDEO_TOP_CURRENTLENGTH, videoTopDownloaded);
    }

    /**
     * 获得顶部下载进度
     */
    public long getVideoTopDownloaded() {
        return getLong(Constant.VIDEO_TOP_CURRENTLENGTH);
    }

    /**
     * 设置顶部视频下载成功
     */
    public void setVideoTopDownloadSuceess(boolean value) {
        setBoolean(Constant.VIDEO_TOP_BOOLEAN, value);
    }

    /**
     * 获得顶部视频下载成功
     */
    public boolean getVideoTopDownloadSuceess() {
        return getBoolean(Constant.VIDEO_TOP_BOOLEAN, false);
    }

    /**
     * 设置当前顶部视频在线播放状态
     */
    public void setVideoTopOnline(boolean online) {
        setBoolean(Constant.VIDEO_TOP_STATE, online);
    }

    /**
     * 获得当前顶部视频在线播放状态
     */
    public boolean getVideoTopOnline() {
        return getBoolean(Constant.VIDEO_BOTTOM_STATE, false);
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
     * 设置底部视频下载进度
     */
    public void setVideoBottomDownloaded(long videoTopDownloaded) {
        setLong(Constant.VIDEO_BOTTOM_CURRENTLENGTH, videoTopDownloaded);
    }

    /**
     * 获得底部下载进度
     */
    public long getVideoBottomDownloaded() {
        return getLong(Constant.VIDEO_BOTTOM_CURRENTLENGTH);
    }

    /**
     * 设置底部视频下载成功
     */
    public void setVideoBottomDownloadSuceess(boolean value) {
        setBoolean(Constant.VIDEO_BOTTOM_BOOLEAN, value);
    }

    /**
     * 获得底部视频下载成功
     */
    public boolean getVideoBottomDownloadSuceess() {
        return getBoolean(Constant.VIDEO_BOTTOM_BOOLEAN, false);
    }

    /**
     * 设置当前底部部视频在线播放状态
     */
    public void setVideoBottomOnline(boolean online) {
        setBoolean(Constant.VIDEO_BOTTOM_STATE, online);
    }

    /**
     * 获得当前底部视频在线播放状态
     */
    public boolean getVideoBottomOnline() {
        return getBoolean(Constant.VIDEO_BOTTOM_STATE, false);
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
     * 设置左侧图片url
     */
    public void setImageLeftUrl(String url) {
        putString(Constant.IMG_LEFT, url);
    }

    /**
     * 获得左侧图片url
     */
    public void getImageLeftUrl() {
        getString(Constant.IMG_LEFT);
    }

    /**
     * 设置中间图片url
     */
    public void setImageCenterUrl(String url) {
        putString(Constant.IMG_CENTER, url);
    }

    /**
     * 获得中间图片url
     */
    public void getImageCenterUrl() {
        getString(Constant.IMG_CENTER);
    }

    /**
     * 设置右侧图片url
     */
    public void setImageRightUrl(String url) {
        putString(Constant.IMG_RIGHT, url);
    }

    /**
     * 获得右侧图片url
     */
    public void getImageRightUrl() {
        getString(Constant.IMG_RIGHT);
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


    /**
     * 设置apk下载状态
     */
    public void setApkDownloadState(boolean state) {
        setBoolean(Constant.APK_DOWNLOAD_STATE, state);
    }

    /**
     * 获得apk下载状态
     */
    public boolean getApkDownloadState() {
        return getBoolean(Constant.APK_DOWNLOAD_STATE, false);
    }

    /**
     * 设置apk下载url
     */
    public void setApkDownloadURL(String url) {
        putString(Constant.APK_DOWNLOAD_URL, url);
    }

    /**
     * 获得apk下载url
     */
    public String getApkDownloadURL() {
        return getString(Constant.APK_DOWNLOAD_URL);
    }

    /**
     * 设置apk下载进度
     */
    public void setApkDownloaded(long apkDownloaded) {
        setLong(Constant.APK_DOWNLOADED, apkDownloaded);
    }

    /**
     * 获得apk下载进度
     */
    public long getApkDownloaded() {
        return getLong(Constant.APK_DOWNLOADED);
    }

}
