package com.yxkj.controller.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

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
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        //给原价设置中划线
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
