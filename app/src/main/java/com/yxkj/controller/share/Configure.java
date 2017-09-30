package com.yxkj.controller.share;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yxkj.controller.R;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.ConfigBean;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by huyong on 2017/9/29.
 */
public class Configure {

    private WeakReference<Application> WRApp;
    private ConfigBean configEntity;

    public Configure(Application application) {
        WRApp = new WeakReference<Application>(application);
    }

    private String config2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(/*System.lineSeparator() +*/ s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * save pdf to target path from raw ,
     */
/*    private void saveRawFile() {
        File pdf = new File(PrintConstants.fullpath);
        if (pdf.exists()) return;
        LogHelper.d("pdf file not found");
        InputStream is = null;
        try {
            is = WRApp.get().getApplicationContext().getResources().openRawResource(R.raw.test_pdf4x6);
            byte[] buffer = new byte[1024];
            int length = 0;
            FileOutputStream fos = new FileOutputStream(PrintConstants.fullpath);
            while (-1 != (length = is.read(buffer))) {
                fos.write(buffer, 0, length);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * test fo save serverconfig file
     */
    public void saveConfig() {
        new Thread() {
            @Override
            public void run() {
                // do write to sd card
                StringBuffer path = new StringBuffer(Environment.getExternalStorageDirectory().getAbsolutePath());
                File file = new File(path + Constant.CONFIGURE_FILE_PATH);
                if (!file.exists()) file.mkdirs();
                File jsonFile = new File(path + Constant.CONFIGURE_FILE_PATH + File.separator + "config.json");
                try {
                    LogUtil.d("json file :" + jsonFile.exists());
                    if (!jsonFile.exists()) { // if json file not exists, just write it
                        jsonFile.createNewFile();
                        InputStream is = WRApp.get().getApplicationContext().getResources().openRawResource(R.raw.config);
                        FileOutputStream fos = new FileOutputStream(jsonFile);
                        int len;
                        byte[] buffer = new byte[1024];
                        while (-1 != (len = is.read(buffer))) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();
                    }

                    // do read sd card
//                    StringBuffer path = new StringBuffer(Environment.getExternalStorageDirectory().getAbsolutePath());
//                    path.append(Constant.CONFIGURE_FILE_PATH + File.separator + "config.json");
                    String temp = config2String(new File(path + Constant.CONFIGURE_FILE_PATH + File.separator + "config.json"));
                    if (TextUtils.isEmpty(temp)) return;
                    LogUtil.d(temp);
                    Gson gson = new Gson();
                    configEntity = gson.fromJson(temp, ConfigBean.class);


//                    if (VERSION != configEntity.getVersion()) {
//                    LogUtil.d("OOB need rewrite json file");
//                    InputStream is = WRApp.get().getApplicationContext().getResources().openRawResource(R.raw.config);
//                    FileOutputStream fos = new FileOutputStream(jsonFile);
//                    int len;
//                    byte[] buffer = new byte[1024];
//                    while (-1 != (len = is.read(buffer))) {
//                        fos.write(buffer, 0, len);
//                    }
//                    is.close();
//                    fos.close();
//
//                    String tempFile = config2String(new File(path.toString()));
//                    if (TextUtils.isEmpty(tempFile)) return;
//                    LogUtil.d(tempFile);
//                    configEntity = gson.fromJson(temp, ConfigBean.class);
////                    }
//                    LogUtil.d("current config.json version:" + configEntity.getVersion());

                    MyApplication.configBean = configEntity;
                    // has target step
                    // mock print file
//                    saveRawFile();
//                    LogHelper.d("mock entity:" + mockEntity.isDebugMode() + "  " + mockEntity.getAccount().getShippingAccount().getPbAccount());
                } catch (Exception e) {
                    LogUtil.e("dump json file fail!!!! reason:" + e.getMessage());
                }
            }
        }.start();
    }

}
