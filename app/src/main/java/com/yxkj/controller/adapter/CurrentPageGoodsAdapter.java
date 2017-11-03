package com.yxkj.controller.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


/**
 * 全部商品列表
 */

public class CurrentPageGoodsAdapter extends BaseRecyclerViewAdapter<SgByChannel> {
    private SelectGoodsListener selectGoodsListener;
    private Map<String, SgByChannel> selectMap = new HashMap<>();

    public Map<String, SgByChannel> getSelectMap() {
        return selectMap;
    }

    public void setSelectMap(Map<String, SgByChannel> selectMap) {
        this.selectMap = selectMap;
        notifyDataSetChanged();
    }

    public void setSelectGoodsListener(SelectGoodsListener selectGoodsListener) {
        this.selectGoodsListener = selectGoodsListener;
    }

    public CurrentPageGoodsAdapter(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_all_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, SgByChannel bean) {
        SgByChannel byCate = selectMap.get(bean.cId + "");
        if (byCate != null) {
            setByCate(holder, byCate);
        } else {
            setByCate(holder, bean);
        }

    }

    private void setByCate(BaseViewHolder holder, final SgByChannel bean) {
        //设置商品名称
        holder.setText(R.id.tv_goods_name, bean.gName);
        //设置商品价格
        holder.setText(R.id.tv_price, "￥" + StringUtil.keepNumberSecondCount(bean.price));
        //设置原价
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        tv_old_price.setVisibility(View.GONE);
        //给原价设置中划线
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        switch (bean.cTemp) {
            case 0://冷冻
                holder.setText(R.id.tv_type, "冷冻");
                holder.setVisible(R.id.tv_type, true);
                break;
            case 1://常温
                holder.setVisible(R.id.tv_type, false);
                break;
        }
        if (bean.count == 0) {
            holder.setVisible(R.id.tv_lack, true);
        } else {
            holder.setVisible(R.id.tv_lack, false);
        }
        //设置商品图片
        ImageView img_name = holder.getView(R.id.img_name);
        GlideUtil.setImageAdvert(context, img_name, bean.gImg,R.mipmap.img_default_all_goods);
        //设置商品选择数量
        NumberAddSubView select_number = holder.getView(R.id.select_number);
        select_number.setMaxValue(bean.count);
        select_number.setValue(bean.number);
        select_number.setOnButtonClickListenter(new NumberAddSubView.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                if (selectGoodsListener != null) {
                    if (bean.number != value) {
                        bean.number = value;
                        selectMap.put(bean.cId + "", bean);
                        selectGoodsListener.select(selectMap, 1);
                    }
                }
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (selectGoodsListener != null) {
                    if (bean.number != value) {
                        bean.number = value;
                        switch (value) {
                            case 0:
                                selectMap.remove(bean.cId);
                                break;
                            default:
                                selectMap.put(bean.cId + "", bean);
                                break;
                        }
                        selectGoodsListener.select(selectMap, 1);
                    }
                }
            }
        });
    }
}
