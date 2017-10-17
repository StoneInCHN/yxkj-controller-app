package com.yxkj.controller.util;

import android.content.Context;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.UrlBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 上位机指令
 */

public class ReciveUrlUtil {
    private static ReciveUrlUtil reciveUrlUtil = null;

    private ReciveUrlUtil(Context context) {

    }

    public static ReciveUrlUtil newInstance() {
        return reciveUrlUtil == null ? new ReciveUrlUtil(MyApplication.getMyApplication()) : reciveUrlUtil;
    }

    public void getJson(String json) {
        Observable.just(json).map(new Function<String, UrlBean>() {
            @Override
            public UrlBean apply(@NonNull String s) throws Exception {
                UrlBean urlBean = new UrlBean();
                JSONObject json = new JSONObject(s);
                Iterator i = json.keys();
                if (i.hasNext()) {
                    urlBean.key = (String) i.next();
                    urlBean.url = json.getString(urlBean.key);
                }
                return urlBean;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<UrlBean>() {
            @Override
            public void accept(@NonNull UrlBean urlBean) throws Exception {
                LogUtil.e(urlBean.toString());
                EventBus.getDefault().post(urlBean);
            }
        });
    }
}
