package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;
import com.yxkj.controller.beans.ByCate;
import com.yxkj.controller.util.StringUtil;

/**
 * 支付页面商品列表适配器
 */

public class PayGoodsAdapter extends BaseRecyclerViewAdapter<ByCate> {
    public PayGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pay_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, ByCate bean) {
        holder.setText(R.id.tv_name, bean.gName);
        holder.setText(R.id.tv_num, bean.cSn);
        holder.setText(R.id.tv_nums, "x" + bean.select);
        holder.setText(R.id.tv_total_price, "￥" + StringUtil.keepNumberSecondCount(bean.select * bean.price));
    }
}
