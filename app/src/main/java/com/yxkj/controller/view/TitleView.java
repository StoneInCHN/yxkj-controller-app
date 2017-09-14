package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.util.TimeCountUtl;

/**
 * 主题导航栏
 */

public class TitleView extends LinearLayout implements TimeCountUtl.CompleteListener {
    /*描述：返回哪里*/
    private TextView tv_desc;
    /*倒计时*/
    private TextView tv_rest_time;
    /*返回图标*/
    private ImageView img_back;
    /*倒计时控制器*/
    private TimeCountUtl timeCountUtl;

    private BackListener backListener;

    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initData();
    }

    /**
     * 初始化View
     */
    private void init() {
        View view = View.inflate(getContext(), R.layout.view_title, null);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_rest_time = view.findViewById(R.id.tv_rest_time);
        img_back = view.findViewById(R.id.img_back);
    }

    /**
     * 初始化配置
     */
    private void initData() {
        timeCountUtl = new TimeCountUtl();
        timeCountUtl.countDown(0, 120, tv_rest_time, "");
        timeCountUtl.setCompleteListener(this);
        img_back.setOnClickListener((view -> {
            if (backListener != null) {
                backListener.onBack();
            }
        }));
        tv_desc.setOnClickListener((view -> {
            if (backListener != null) {
                backListener.onBack();
            }
        }));
    }

    @Override
    public void onComplete() {
        if (backListener != null) {
            backListener.onBack();
        }
    }

    public interface BackListener {
        void onBack();
    }
}
