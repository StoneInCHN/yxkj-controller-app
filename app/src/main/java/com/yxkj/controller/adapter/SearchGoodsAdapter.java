package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;


/**
 * 搜索商品列表
 */

public class SearchGoodsAdapter extends BaseRecyclerViewAdapter<String> {
    public SearchGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, String bean) {

    }

}
