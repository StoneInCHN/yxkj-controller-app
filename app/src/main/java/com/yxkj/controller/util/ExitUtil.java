package com.yxkj.controller.util;

import com.yxkj.controller.callback.ShowInputPwdCallBack;

/**
 *
 */

public class ExitUtil {
    private int text = 0;//点击文本框次数
    private int delete = 0;//点击删除键次数
    private int clear = 0;//点击重置键次数
    private ShowInputPwdCallBack showInputPwdCallBack;

    public void setShowInputPwdCallBack(ShowInputPwdCallBack showInputPwdCallBack) {
        this.showInputPwdCallBack = showInputPwdCallBack;
    }

    public ExitUtil() {
    }

    //点击文本框
    public void textAdd() {
        if (text > 6) {
            clear();
        }
        //点击了文本框，清空另外两个
        delete = 0;
        clear = 0;
        text++;
    }

    //点击删除键
    public void deleteAdd() {
        //如果文本框次数不足六或者delete大于6，清空
        if (text != 6 || delete > 6) {
            clear();
        }
        delete++;
    }

    //点击清空键
    public void clearAdd() {
        //如果删除键次数或者文本框不足6,清空
        if (delete != 6 || text != 6) {
            clear();
        }
        clear++;
        //结束流程清空，回调
        if (clear == 6) {
            clear();
            if (showInputPwdCallBack != null) {
                showInputPwdCallBack.onShowInputPwd();
            }
        }
    }

    //重置
    public void clear() {
        text = 0;
        delete = 0;
        clear = 0;
    }

}
