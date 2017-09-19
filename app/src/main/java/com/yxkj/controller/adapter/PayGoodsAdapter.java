package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;

/**
 * 支付页面商品列表适配器
 */

public class PayGoodsAdapter extends BaseRecyclerViewAdapter<String> {
    public PayGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pay_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, String bean) {

    }
}
