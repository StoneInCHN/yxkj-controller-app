package com.yxkj.controller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;
import com.yxkj.controller.beans.GoodsSelectInfo;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.util.GlideUtil;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.view.NumberAddSubView;

import org.greenrobot.eventbus.EventBus;


/**
 * 搜索商品列表
 */

public class SearchGoodsAdapter extends BaseRecyclerViewAdapter<SgByChannel> {
    private double total_price;//总价
    private boolean enable = true;

    public void setEnable(boolean enable) {
        this.enable = enable;
        notifyDataSetChanged();
    }

    public void setTotal_price(double total_price) {
        this.total_price += total_price;
        EventBus.getDefault().post(new GoodsSelectInfo(this.total_price, tList.size()));
    }

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
        if (enable) {
            holder.setVisible(R.id.tv_number, false);
            holder.setVisible(R.id.selcct_number, true);
        } else {
            holder.setVisible(R.id.tv_number, true);
            holder.setVisible(R.id.selcct_number, false);
        }
        holder.setText(R.id.tv_number, "x " + bean.number);
        //显示图片
        GlideUtil.setImageAdvert(context, img_name, bean.gImg, R.mipmap.img_default);
        //商品编号
        holder.setText(R.id.tv_num, bean.cSn);
        //商品名称
        holder.setText(R.id.tv_name, bean.gName);
        //设置选择数量
        selcct_number.setValue(bean.number);
        selcct_number.setMaxValue(bean.count);
        selcct_number.setOnButtonClickListenter(new NumberAddSubView.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                if (value > bean.number) {
                    bean.number = value;
                    //加一，总价加一
                    total_price += bean.price;
                    //设置该项总价
                    holder.setText(R.id.tv_total_price, "￥" + StringUtil.keepNumberSecondCount(bean.price * value));
                    EventBus.getDefault().post(new GoodsSelectInfo(total_price, tList.size()));
                }
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                bean.number = value;
                //减一，总价减一
                total_price -= bean.price;
                switch (value) {
                    case 0:
                        EventBus.getDefault().post(new GoodsSelectInfo(total_price, tList.size(), true));
                        if (tList.size() > 1) {
                            tList.remove(bean);
                            notifyDataSetChanged();
                        }
                        break;
                    default:
                        //设置该项总价
                        EventBus.getDefault().post(new GoodsSelectInfo(total_price, tList.size()));
                        holder.setText(R.id.tv_total_price, "￥" + StringUtil.keepNumberSecondCount(bean.price * value));
                        break;
                }
            }
        });
        //设置该项总价
        holder.setText(R.id.tv_total_price, "￥" + StringUtil.keepNumberSecondCount(bean.price * selcct_number.getValue()));
    }

    public void clearTotal() {
        total_price = 0;
    }
}
