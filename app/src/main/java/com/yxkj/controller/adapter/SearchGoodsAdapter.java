package com.yxkj.controller.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.util.GlideUtil;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.view.NumberAddSubView;


/**
 * 搜索商品列表
 */

public class SearchGoodsAdapter extends BaseRecyclerViewAdapter<SgByChannel> {
    public SearchGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, SgByChannel bean) {
        ImageView img_name = holder.getView(R.id.img_name);
        NumberAddSubView selcct_number = holder.getView(R.id.selcct_number);
        //显示图片
        GlideUtil.setImage(context, img_name, bean.gImg);
        //商品编号
        holder.setText(R.id.tv_num, bean.cSn);
        //商品名称
        holder.setText(R.id.tv_name, bean.gName);
        //设置选择数量
        selcct_number.setValue(bean.number);
        selcct_number.setMaxValue(bean.count);
        //设置该项总价
        holder.setText(R.id.tv_total_price, StringUtil.keepNumberSecondCount(bean.number * selcct_number.getValue() + ""));
    }

}
