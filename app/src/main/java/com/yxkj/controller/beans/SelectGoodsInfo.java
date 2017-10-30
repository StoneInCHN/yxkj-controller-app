package com.yxkj.controller.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择商品信息
 */

public class SelectGoodsInfo {
    public double price;
    public List<SgByChannel> list = new ArrayList<>();

    @Override
    public String toString() {
        return "SelectGoodsInfo{" +
                "price=" + price +
                ", list=" + list +
                '}';
    }
}
