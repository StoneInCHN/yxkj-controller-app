package com.yxkj.controller.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.body.ProgressResponseBody;
import com.yxkj.controller.callback.ProgressListener;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.share.SharePrefreceHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * Apk下载类
 */

public class ApkDownloadUtil implements ProgressListener {
    private static ApkDownloadUtil downloadUtil;
    private OkHttpClient okHttpClient;
    private Call call;
    private long downloadedLength;
    private File file;

    public static ApkDownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new ApkDownloadUtil();
        }
        return downloadUtil;
    }

    private ApkDownloadUtil() {
        okHttpClient = getProgressClient();
        file = new File(Environment.getExternalStorageDirectory() + File.separator + "test.apk");
    }

//    /**
//     * @param url 下载连接
//     */
//    public void download(final String url) {
//        if (RootUtils.isRooted()) {
//            String saveDir = Environment.getExternalStorageDirectory() + File.separator + "test.apk";
//            Request request = new Request.Builder().url(url).build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    // 下载失败
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    InputStream is = null;
//                    byte[] buf = new byte[2048];
//                    int len = 0;
//                    FileOutputStream fos = null;
//                    try {
//                        is = response.body().byteStream();
//                        long total = response.body().contentLength();
//                        LogUtil.d("TAG" + total);
//                        File file = new File(saveDir);
//                        fos = new FileOutputStream(file);
//                        long sum = 0;
//                        while ((len = is.read(buf)) != -1) {
//                            fos.write(buf, 0, len);
//                            sum += len;
//                        }
//                        fos.flush();
//                        // 下载完成
//                        install(saveDir);
//                    } catch (Exception e) {
//                        LogUtil.e(e.toString());
//                    } finally {
//                        try {
//                            if (is != null)
//                                is.close();
//                        } catch (IOException e) {
//                        }
//                        try {
//                            if (fos != null)
//                                fos.close();
//                        } catch (IOException e) {
//                        }
//                    }
//                }
//            });
//        }
//    }

    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @return 安装成功返回true，安装失败返回false。
     */
    public boolean install() {
        boolean result = false;
        Process process = null;
        OutputStream out = null;
        Log.i("TAG", "file.getPath()：" + file.getPath());
        if (file.exists()) {
            System.out.println(file.getPath() + "==");
            try {
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                // 获取文件所有权限
                dataOutputStream.writeBytes("chmod 777 " + file.getPath()
                        + "\n");
                // 进行静默安装命令
                dataOutputStream
                        .writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
                                + file.getPath());
                dataOutputStream.flush();
                // 关闭流操作
                dataOutputStream.close();
                out.close();
                int value = process.waitFor();

                // 代表成功
                if (value == 0) {
                    Log.i("TAG", "安装成功！");
                    long record_id = SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).getRestart();
                    if (record_id != 0) {
                        HttpFactory.updateCmdStatus(record_id, true, null);
                        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setRestart(0);
                    }
                    result = true;
                    // 失败
                } else if (value == 1) {
                    Log.i("TAG", "安装失败！");
                    long record_id = SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).getRestart();
                    if (record_id != 0) {
                        HttpFactory.updateCmdStatus(record_id, false, null);
                        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setRestart(0);
                    }
                    result = false;
                    // 未知情况
                } else {
                    Log.i("TAG", "未知情况！");
                    long record_id = SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).getRestart();
                    if (record_id != 0) {
                        HttpFactory.updateCmdStatus(record_id, false, null);
                        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setRestart(0);
                    }
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param url 下载连接
     */
    public void download(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setApkDownloadState(false);
        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setApkDownloadURL(url);
        downloadedLength = getSharePrefreceHelper().getApkDownloaded();
        call = newCall(downloadedLength, url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!getSharePrefreceHelper().getApkDownloadState())
                    getSharePrefreceHelper().setApkDownloaded(downloadedLength);
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
                getSharePrefreceHelper().setApkDownloadState(true);
                if (RootUtils.isRooted())
                    install();
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
        if (!getSharePrefreceHelper().getApkDownloadState()) {
            if (call != null) {
                call.cancel();
                getSharePrefreceHelper().setApkDownloaded(downloadedLength);
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
                        .body(new ProgressResponseBody(originalResponse.body(), ApkDownloadUtil.this))
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
    }

    private SharePrefreceHelper getSharePrefreceHelper() {
        return SharePrefreceHelper.getInstence(MyApplication.getMyApplication());
    }
}
