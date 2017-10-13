package com.yxkj.controller.util;

import org.json.JSONObject;

import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */

public class ReciveUrlUtil {
    private static final String VIDEO_TOP = "video_top";
    private static final String VIDEO_BOTTOM = "video_bottom";
    private static final String IMG_LEFT = "img_left";
    private static final String IMG_CENTER = "img_center";
    private static final String IMG_RIGHT = "img_right";

    public ReciveUrlUtil(String json) {
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
                handleData(urlBean);
            }
        });
    }


    public void handleData(UrlBean urlBean) {
        switch (urlBean.key) {
            case VIDEO_TOP:
                break;
            case VIDEO_BOTTOM:
                break;
            case IMG_LEFT:
                break;
            case IMG_CENTER:
                break;
            case IMG_RIGHT:
                break;
        }
    }

    class UrlBean {
        String key;
        String url;

        @Override
        public String toString() {
            return "UrlBean{" +
                    "key='" + key + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
