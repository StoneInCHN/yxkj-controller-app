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
    protected void convert(BaseViewHolder holder, String item, int position) {

    }

    @Override
    protected int getItemViewLayoutId(int position, String item) {
        return R.layout.item_test;
    }
}
