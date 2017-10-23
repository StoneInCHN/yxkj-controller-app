package com.yxkj.controller.adapter;

import android.content.Context;
import android.view.View;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;
import com.yxkj.controller.beans.ByCate;
import com.yxkj.controller.callback.SelectGoodsListener;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.view.NumberAddSubView;

import java.util.HashMap;
import java.util.Map;

/**
 * 已选择商品列表
 */

public class SelectedGoodsAdapter extends BaseRecyclerViewAdapter<ByCate> {
    private SelectGoodsListener selectGoodsListener;
    private Map<String, ByCate> selectMap = new HashMap<>();

    public void setSelectGoodsListener(SelectGoodsListener selectGoodsListener) {
        this.selectGoodsListener = selectGoodsListener;
    }

    public SelectedGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_selected_goods;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, ByCate bean) {
        selectMap.put(bean.cId, bean);
        holder.setText(R.id.tv_name, bean.gName);
        holder.setText(R.id.tv_price, "￥" + StringUtil.keepNumberSecondCount(bean.price));
        NumberAddSubView numberAddSubView = holder.getView(R.id.selcct_number);
        numberAddSubView.setValue(bean.select);
        numberAddSubView.setMaxValue(bean.count);
        numberAddSubView.setOnButtonClickListenter(new NumberAddSubView.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                if (selectGoodsListener != null) {
                    bean.select = value;
                    selectMap.put(bean.cId, bean);
                    selectGoodsListener.select(selectMap, 2);
                }
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (selectGoodsListener != null) {
                    bean.select = value;
                    switch (value) {
                        case 0:
                            selectMap.remove(bean.cId);
                            break;
                        default:
                            selectMap.put(bean.cId, bean);
                            break;
                    }
                    selectGoodsListener.select(selectMap, 2);
                }
            }
        });
    }

    public void clear() {
        tList.clear();
        selectMap.clear();
        notifyDataSetChanged();
        if (selectGoodsListener != null) {
            selectGoodsListener.select(selectMap, 2);
        }
    }
}
