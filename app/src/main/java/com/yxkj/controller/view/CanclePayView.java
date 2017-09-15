package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yxkj.controller.R;
import com.yxkj.controller.callback.SelectListener;

/**
 * 取消支付
 */

public class CanclePayView extends LinearLayout {
    /*确定按钮*/
    private Button btn_sure;
    /*取消按钮*/
    private Button btn_cancle;
    /*选择监听*/
    private SelectListener selectListener;

    public CanclePayView(Context context) {
        this(context, null);
    }

    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public CanclePayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanclePayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_canle_pay, null);
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_cancle = view.findViewById(R.id.btn_cancle);
        addView(view);
        btn_sure.setOnClickListener((view1) -> {
            if (selectListener != null) {
                selectListener.onSure();//点击了确认
            }
        });
        btn_cancle.setOnClickListener((view1 -> {
            if (selectListener != null) {
                selectListener.onCancle();//点击了取消
            }
        }));
    }

}
