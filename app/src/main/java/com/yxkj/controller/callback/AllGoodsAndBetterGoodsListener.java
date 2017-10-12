package com.yxkj.controller.callback;

import com.yxkj.controller.view.AllGoodsPopupWindow;

/**
 * 点击了优质或者全部商品
 */
public interface AllGoodsAndBetterGoodsListener {
    void onAllGoods(AllGoodsPopupWindow allGoodsPopupWindow);

    void onBetterGoods();
}
