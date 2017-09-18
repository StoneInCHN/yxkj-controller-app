package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;

/**
 * 已选择商品列表
 */

public class SelectedGoodsAdapter extends BaseRecyclerViewAdapter<String> {
    public SelectedGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_selected_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, String bean) {

    }
}
