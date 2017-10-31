package com.yxkj.controller.util;

import android.content.Context;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.UrlBean;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.share.SharePrefreceHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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

    public void getJson(Map<String, String> map) {
        Observable.fromArray(map).map(new Function<Map<String, String>, UrlBean>() {
            @Override
            public UrlBean apply(@NonNull Map<String, String> map) throws Exception {
                UrlBean urlBean = new UrlBean();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    urlBean.key = entry.getKey();
                    urlBean.url = entry.getValue();
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

    /**
     * 支付成功
     */
    public void onPaySuccess() {
        UrlBean urlBean = new UrlBean();
        urlBean.key = Constant.PAYSUCCESS;
        EventBus.getDefault().post(urlBean);
    }

    /**
     * 重启
     */
    public void reStartSystem(long record_id) {
        UrlBean urlBean = new UrlBean();
        urlBean.key = Constant.RESTARTSYSTEM;
        urlBean.record_id = record_id;
        SharePrefreceHelper.getInstence(MyApplication.getMyApplication()).setRestart(urlBean.record_id);
        EventBus.getDefault().post(urlBean);
    }
}
