package com.yxkj.controller.beans;

/**
 * 搜索列表选择商品信息
 */

public class GoodsSelectInfo {
    public double total_price;
    public int size;
    public boolean isClear;

    public GoodsSelectInfo(double total_price, int size) {
        this.total_price = total_price;
        this.size = size;
    }

    public GoodsSelectInfo(double total_price, int size, boolean isClear) {
        this.total_price = total_price;
        this.size = size;
        this.isClear = isClear;
    }

    @Override
    public String toString() {
        return "GoodsSelectInfo{" +
                "total_price=" + total_price +
                ", size=" + size +
                ", isClear=" + isClear +
                '}';
    }
}
