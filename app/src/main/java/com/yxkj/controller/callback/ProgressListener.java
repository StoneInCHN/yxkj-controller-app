package com.yxkj.controller.callback;

/**
 * 下载进度
 */
public interface ProgressListener {
    void onPreExecute(long contentLength);

    void update(long totalBytes, boolean done);
}
