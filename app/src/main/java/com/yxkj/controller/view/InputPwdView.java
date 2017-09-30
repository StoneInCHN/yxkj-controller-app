package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.callback.InputManagerPwdListener;

/**
 * 管理员密码弹窗
 */

public class InputPwdView extends LinearLayout {
    private Button btn_cancle, btn_sure;
    private TextView tv_pwd_error;
    private EditText et_pwd;
    private ImageView img_cancle;
    private InputManagerPwdListener inputManagerPwdListener;

    public void setInputManagerPwdListener(InputManagerPwdListener inputManagerPwdListener) {
        this.inputManagerPwdListener = inputManagerPwdListener;
    }

    public InputPwdView(Context context) {
        this(context, null);
    }

    public InputPwdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputPwdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = View.inflate(getContext(), R.layout.view_manager_pwd, null);
        img_cancle = view.findViewById(R.id.img_cancle);
        et_pwd = view.findViewById(R.id.et_pwd);
        tv_pwd_error = view.findViewById(R.id.tv_pwd_error);
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_cancle = view.findViewById(R.id.btn_cancle);
        addView(view);
        btn_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pwd.setText("");
                if (inputManagerPwdListener != null) {
                    inputManagerPwdListener.onClickCancle();
                }
            }
        });
        img_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pwd.setText("");
                if (inputManagerPwdListener != null) {
                    inputManagerPwdListener.onClickCancle();
                }
            }
        });
        btn_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = et_pwd.getText().toString();
                if (!pwd.equals("123456")) {
                    tv_pwd_error.setVisibility(VISIBLE);
                    return;
                } else {
                    tv_pwd_error.setVisibility(INVISIBLE);
                }
                if (inputManagerPwdListener != null) {
                    inputManagerPwdListener.onClickSure();
                }
            }
        });

    }


}
