package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;


/**
 * 全部商品列表
 */

public class CurrentPageGoodsAdapter extends BaseRecyclerViewAdapter<String> {

    public CurrentPageGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_all_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, String bean) {
        holder.setText(R.id.tv_goods_name, "香飘飘");
    }

}
