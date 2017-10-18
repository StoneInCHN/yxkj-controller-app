package com.yxkj.controller.util;

import android.os.Environment;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.share.SharePrefreceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 视频下载类
 */

public class VideoDownloadUtil {
    private static VideoDownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static VideoDownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new VideoDownloadUtil();
        }
        return downloadUtil;
    }

    private VideoDownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */
    public void download(final String url, final String saveDir, int type) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(MyApplication.getMyApplication().getExternalFilesDir(null) + File.separator + saveDir);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
//                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    switch (type) {
                        case 0:
                            SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setVideoTopDownloadSuceess(true);
                            break;
                        case 1:
                            SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setVideoBottomDownloadSuceess(true);
                            break;
                    }

//                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
//                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
