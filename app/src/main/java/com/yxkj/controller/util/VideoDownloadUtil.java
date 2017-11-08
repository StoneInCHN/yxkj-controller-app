package com.yxkj.controller.util;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.body.ProgressResponseBody;
import com.yxkj.controller.callback.ProgressListener;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.share.SharePrefreceHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 视频下载类
 */

public class VideoDownloadUtil implements ProgressListener {
    private static VideoDownloadUtil videoDownLoad = null;
    private OkHttpClient okHttpClient;
    private Call call;
    private long downloadedLength;
    private File file;

    public static VideoDownloadUtil get() {
        if (videoDownLoad == null) {
            videoDownLoad = new VideoDownloadUtil();
        }
        return videoDownLoad;
    }

    private VideoDownloadUtil() {
        okHttpClient = getProgressClient();
        file = new File(Constant.VIDEO_TOP_ADDRESS);
    }

    /**
     * @param url 下载连接
     */
    public void download(final String url) {
        downloadedLength = getSharePrefreceHelper().getVideoTopDownloaded();
        call = newCall(downloadedLength, url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!getSharePrefreceHelper().getVideoTopDownloadSuceess())
                    getSharePrefreceHelper().setVideoTopDownloaded(downloadedLength);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                save(response, downloadedLength);
            }
        });
    }


    /**
     * 保存到sd卡
     *
     * @param response
     */
    private void save(Response response, long startPosition) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startPosition, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
            if (downloadedLength == body.contentLength()) {
                getSharePrefreceHelper().setVideoTopDownloadSuceess(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停下载
     */
    public void pause() {
        if (!getSharePrefreceHelper().getVideoTopDownloadSuceess()) {
            if (call != null) {
                call.cancel();
                getSharePrefreceHelper().setVideoTopDownloaded(downloadedLength);
            }
        }
    }

    //每次下载需要新建新的Call对象
    private Call newCall(long startPoints, String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startPoints + "-")//断点续传要用到的，指示下载的区间
                .build();
        return okHttpClient.newCall(request);
    }

    public OkHttpClient getProgressClient() {
        // 拦截器，用上ProgressResponseBody
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), VideoDownloadUtil.this))
                        .build();
            }
        };

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    @Override
    public void onPreExecute(long contentLength) {

    }

    @Override
    public void update(long totalBytes, boolean done) {
        downloadedLength = totalBytes;
        LogUtil.w("readed: ", totalBytes + "-------------total: " + "------------file: " + file.length() + "------done:  " + done);
    }

    private SharePrefreceHelper getSharePrefreceHelper() {
        return SharePrefreceHelper.getInstence(MyApplication.getMyApplication());
    }
}
