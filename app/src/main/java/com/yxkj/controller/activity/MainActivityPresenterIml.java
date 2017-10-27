package com.yxkj.controller.activity;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.share.SharePrefreceHelper;
import com.yxkj.controller.util.ApkDownloadUtil;
import com.yxkj.controller.util.VideoBottomDownloadUtil;
import com.yxkj.controller.util.VideoDownloadUtil;

import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM_DEFAULT_URL;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP_DEFAULT_URL;

/**
 *
 */

public class MainActivityPresenterIml implements MainActivityPresenter {


    @Override
    public void setPlayFileVideo(Context context, IjkVideoView videoView, String file) {
        videoView.initVideoView(context);
        videoView.setVideoURI(Uri.parse(file));
        videoView.start();
    }

    @Override
    public void downloadVideo(String url, int type) {
        switch (type) {
            case 0:
                VideoDownloadUtil.get().download(url);
                break;
            case 1:
                VideoBottomDownloadUtil.get().download(url);
                break;
        }
    }

    @Override
    public void initVideo(Context context, IjkVideoView videoView, int type) {
        switch (type) {
            case 0:
                if (!SharePrefreceHelper.getInstence(context).getVideoTopDownloadSuceess()) { //（顶部视频）初次进来下载，如果没有下载过，就下载，
                    SharePrefreceHelper.getInstence(context).setVideoTopOnline(true);
                    String top_url = SharePrefreceHelper.getInstence(context).getVideoTopUrl();//获取顶部视频url地址
                    if (TextUtils.isEmpty(top_url)) {//如果为空，就在线播放并下载
                        videoView.setVideoURI(Uri.parse(VIDEO_TOP_DEFAULT_URL));
                        SharePrefreceHelper.getInstence(context).setVideoTopUrl(VIDEO_TOP_DEFAULT_URL);
                        downloadVideo(VIDEO_TOP_DEFAULT_URL, 0);
                    } else {
                        videoView.setVideoURI(Uri.parse(top_url));
                        downloadVideo(top_url, 0);
                    }
                } else {
                    SharePrefreceHelper.getInstence(context).setVideoTopOnline(false);
                    setPlayFileVideo(context, videoView, Constant.VIDEO_TOP_ADDRESS);//已经下载过顶部视频，所以直接通过本地地址播放，
                }
                break;
            case 1:
                if (!SharePrefreceHelper.getInstence(context).getVideoBottomDownloadSuceess()) {//（底部视频）初次进来下载，如果没有下载过，就下载，
                    String bottom_url = SharePrefreceHelper.getInstence(context).getVideoBottomUrl();//获取底部视频url地址
                    SharePrefreceHelper.getInstence(context).setVideoBottomOnline(true);
                    if (TextUtils.isEmpty(bottom_url)) {//如果为空，就在线播放并下载
                        videoView.setVideoURI(Uri.parse(VIDEO_BOTTOM_DEFAULT_URL));
                        SharePrefreceHelper.getInstence(context).setVideoBottomUrl(VIDEO_BOTTOM_DEFAULT_URL);
                        downloadVideo(VIDEO_BOTTOM_DEFAULT_URL, 1);
                    } else {
                        videoView.setVideoURI(Uri.parse(bottom_url));
                        downloadVideo(bottom_url, 1);
                    }
                } else {
                    SharePrefreceHelper.getInstence(context).setVideoBottomOnline(false);
                    setPlayFileVideo(context, videoView, Constant.VIDEO_BOTTOM_ADDRESS);//已经下载过底部视频，所以直接通过本地地址播放，
                }
                break;
        }
    }

    @Override
    public void onConnected(Context context) {
        if (!SharePrefreceHelper.getInstence(context).getApkDownloadState()) {
            String url = SharePrefreceHelper.getInstence(context).getApkDownloadURL();
            if (!TextUtils.isEmpty(url))
                ApkDownloadUtil.get().download(url);
        }
    }

    @Override
    public void onDisconnected() {
        VideoDownloadUtil.get().pause();
        VideoBottomDownloadUtil.get().pause();
        ApkDownloadUtil.get().pause();
    }

    @Override
    public void orderFromServerDownLoadTopVideo(Context context, String url) {
        SharePrefreceHelper.getInstence(context).setVideoTopOnline(true);
        SharePrefreceHelper.getInstence(context).setVideoTopDownloadSuceess(false);
        SharePrefreceHelper.getInstence(context).setVideoTopUrl(url);
    }

    @Override
    public void orderFromServerDownLoadBottomVideo(Context context, String url) {
        SharePrefreceHelper.getInstence(context).setVideoBottomOnline(true);
        SharePrefreceHelper.getInstence(context).setVideoBottomDownloadSuceess(false);
        SharePrefreceHelper.getInstence(context).setVideoBottomUrl(url);
    }
}
