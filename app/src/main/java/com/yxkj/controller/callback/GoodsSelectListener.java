package com.yxkj.controller.callback;

/**
 * 商品选择回调
 */

public interface GoodsSelectListener<T> {
    void onGoodsSelect(T goods);
}
