package com.yxkj.controller.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yxkj.controller.R;
import com.yxkj.controller.util.NetUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends RxFragment implements View.OnClickListener {
    public String TAG = getClass().getSimpleName();
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getResource(), null);
            EventBus.getDefault().register(this);
            beforeInitView();
            initView(rootView);
            initData();
            setEvent();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    public <T extends View> T findViewByIdNoCast(int id) {
        return rootView == null ? null : (T) rootView.findViewById(id);
    }

    public abstract int getResource();

    public abstract void beforeInitView();

    public abstract void initView(View rootView);

    public abstract void initData();

    public abstract void setEvent();

    /**
     * 根据id设置点击事件
     */
    public void setOnClick(Object... objects) {
        for (Object object : objects) {
            if (object instanceof View) {
                ((View) object).setOnClickListener(this);
            }
            if (object instanceof Integer) {
                findViewByIdNoCast((Integer) object).setOnClickListener(this);
            }

        }
    }

    /**
     * 线程调度
     */
    protected <T> ObservableTransformer<T, T> compose(final LifecycleTransformer<T> lifecycle) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        // 可添加网络连接判断等
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            Toast.makeText(getActivity(), R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).compose(lifecycle);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
