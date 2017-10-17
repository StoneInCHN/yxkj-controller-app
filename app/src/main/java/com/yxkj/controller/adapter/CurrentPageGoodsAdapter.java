package com.yxkj.controller.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;
import com.yxkj.controller.beans.ByCate;
import com.yxkj.controller.util.GlideUtil;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.view.NumberAddSubView;


/**
 * 全部商品列表
 */

public class CurrentPageGoodsAdapter extends BaseRecyclerViewAdapter<ByCate> {

    public CurrentPageGoodsAdapter(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_all_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, ByCate bean) {
        //设置商品名称
        holder.setText(R.id.tv_goods_name, bean.gName);
        //设置商品价格
        holder.setText(R.id.tv_price, StringUtil.keepNumberSecondCount(bean.price + ""));
        //设置原价
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        tv_old_price.setVisibility(View.GONE);
        //给原价设置中划线
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        switch (bean.cTemp) {
            case 0://冷冻
                holder.setText(R.id.tv_type, "冷冻");
                break;
            case 1://常温
                holder.setText(R.id.tv_type, "常温");
                break;
        }
        //设置商品图片
        ImageView img_name = holder.getView(R.id.img_name);
        GlideUtil.setImage(context, img_name, bean.gImg);
        //设置商品选择数量
        NumberAddSubView select_number = holder.getView(R.id.select_number);
        select_number.setMaxValue(bean.count);
        select_number.setValue(bean.select);

    }

}
