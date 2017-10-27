package com.yxkj.controller.activity;

import android.content.Context;

import com.dou361.ijkplayer.widget.IjkVideoView;

/**
 *
 */

public interface MainActivityPresenter {

    /**
     * 设置播放视频
     */
    void setPlayFileVideo(Context context, IjkVideoView videoView, String file);

    /**
     * 下载视频
     * type 0、顶部视频 1、底部视频
     */
    void downloadVideo(String url, int type);

    /**
     * 视频初始化下载配置
     *
     * @param videoView
     * @param type
     */
    void initVideo(Context context, IjkVideoView videoView, int type);

    /**
     * 网络连接
     */
    void onConnected(Context context);

    /**
     * 网络断开
     */
    void onDisconnected();

    /**
     * 下载顶部视频指令
     */
    void orderFromServerDownLoadTopVideo(Context context,String url);


    /**
     * 下载底部视频指令
     */
    void orderFromServerDownLoadBottomVideo(Context context,String url);
}
