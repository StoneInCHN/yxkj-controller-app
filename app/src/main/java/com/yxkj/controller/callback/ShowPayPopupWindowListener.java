package com.yxkj.controller.callback;

import android.graphics.Bitmap;

import com.yxkj.controller.beans.SgByChannel;

import java.util.List;

/**
 * 显示支付PopWindow
 */

public interface ShowPayPopupWindowListener {
    void showPayPopWindow(List<SgByChannel> byCateList, double price, Bitmap bitmap);
}
