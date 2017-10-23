package com.yxkj.controller.callback;

import com.yxkj.controller.beans.ByCate;

import java.util.Map;

/**
 * type 1、全部商品 2、来自已选商品
 */

public interface SelectGoodsListener {
    void select(Map<String, ByCate> selectMap, int type);
}
