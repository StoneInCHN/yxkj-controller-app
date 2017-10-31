package com.yxkj.controller.base;

import android.accounts.NetworkErrorException;

import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ToastUtil;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Observer基类
 */
public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {


    protected BaseObserver() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        LogUtil.e("onStart:");
    }

    @Override
    public void onNext(BaseEntity<T> value) {
        LogUtil.e("values  " + value.toString());
        if (value.code == 0000) {
            T t = value.msg;
            onHandleSuccess(t);
        } else if (value.code == 1000) {
            ToastUtil.showToast(value.desc);
            T t = value.msg;
            onHandleStockNotEnough(t);
        } else {
            onHandleError(value.desc);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("onError:" + e.toString());
        ToastUtil.showToast("网络错误");
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        LogUtil.e("onComplete");
    }

    protected void onHandleStockNotEnough(T value) {

    }

    protected void onHandleError(String msg) {
        ToastUtil.showToast(msg);
    }

    protected abstract void onHandleSuccess(T t);

    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;
}
