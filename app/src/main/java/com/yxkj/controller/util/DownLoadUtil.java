package com.yxkj.controller.util;

import android.os.Looper;

import com.yxkj.controller.base.BaseActivity;
import com.yxkj.controller.base.BaseFragment;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.http.RetrofitService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 下载类
 */

public class DownLoadUtil {
    private BaseActivity activity;
    private BaseFragment fragment;
    private String fileName;
    private long totalSize;
    private long currentDownload;

    public DownLoadUtil(BaseActivity activity, String fileName) {
        this.activity = activity;
        this.fileName = fileName;
    }

    public DownLoadUtil(BaseFragment fragment, String fileName) {
        this.fragment = fragment;
        this.fileName = fileName;
    }

    /**
     * 获取总共大小
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * 获取当前下载
     */
    public long getCurrentDownloaded() {
        return currentDownload;
    }

    /**
     * 开始下载
     */
    public void startDownload() {
        // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor((Interceptor.Chain chain) -> {
                    okhttp3.Response orginalResponse = chain.proceed(chain.request());
                    return orginalResponse.newBuilder().body(new ProgressResponseBody(orginalResponse.body(), (long progress, long total, boolean done) -> {
                                totalSize = total;
                                currentDownload = progress;
                                LogUtil.e(Looper.myLooper() + "");
                                LogUtil.e("onProgress: " + "total ---->" + total + "done ---->" + progress);
                            })
                    ).build();
                })
                /*
                 *  这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
                 *  出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
                 */
                .addInterceptor(new HttpLoggingInterceptor(message -> LogUtil.e(message)).setLevel(HttpLoggingInterceptor.Level.BASIC))
                .connectTimeout(Constant.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.TIMEOUT, TimeUnit.SECONDS)
                .build();

        RetrofitService retrofitService = new Retrofit.Builder()
                .baseUrl(Constant.url_sougou)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(RetrofitService.class);

        retrofitService.download().subscribeOn(Schedulers.io()).subscribe(responseBodyResponse -> {
            if (responseBodyResponse.isSuccessful()) {
                writeResponseBodyToDisk(responseBodyResponse.body());//下载成功之后，保存到本地
            }
        }, throwable -> LogUtil.e(throwable.toString()));
    }

    /**
     * 保存到sd卡
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(activity.getExternalFilesDir(null) + File.separator + fileName);
            LogUtil.e(futureStudioIconFile.toString());
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                int fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    //静默安装
    private void installSlient(File flie) {
        String cmd = "pm install -r " + flie.toString();
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果
    }

    public class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener listener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener listener) {
            this.responseBody = responseBody;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (null == bufferedSource) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    listener.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    public interface ProgressListener {
        /**
         * @param progress 已经下载或上传字节数
         * @param total    总字节数
         * @param done     是否完成
         */
        void onProgress(long progress, long total, boolean done);
    }
}
