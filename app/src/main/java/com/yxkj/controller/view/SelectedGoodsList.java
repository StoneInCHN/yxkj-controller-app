package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.SelectedGoodsAdapter;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.callback.SelectGoodsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已选择商品列表布局
 */

public class SelectedGoodsList extends FrameLayout implements SelectGoodsListener {
    /*已选择的商品列表*/
    private RecyclerView recyclerView_selected;
    /*已选择商品列表适配器*/
    private SelectedGoodsAdapter selectedGoodsAdapter;
    private SelectGoodsListener selectGoodsListener;
    /*清空列表*/
    private TextView tv_clear;
    /*选择商品列表*/
    private List<SgByChannel> selectedGoods = new ArrayList<>();
    /*判断是否显示*/
    private boolean isShow;

    public void setSelectGoodsListener(SelectGoodsListener selectGoodsListener) {
        this.selectGoodsListener = selectGoodsListener;
    }

    public SelectedGoodsList(Context context) {
        this(context, null);
    }

    public SelectedGoodsList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedGoodsList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectedGoodsAdapter = new SelectedGoodsAdapter(getContext());
        selectedGoodsAdapter.setSelectGoodsListener(this);
        init();
        recyclerView_selected.setAdapter(selectedGoodsAdapter);
    }

    /**
     * 初始化View
     */
    private void init() {
        View view = View.inflate(getContext(), R.layout.view_selected_goods, this);
        recyclerView_selected = view.findViewById(R.id.recyclerView_selected);
        tv_clear = view.findViewById(R.id.tv_clear);
        recyclerView_selected.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                togle();
                clearList();
            }
        });
    }

    /**
     * 选择的商品列表数据
     *
     * @param selectedGoods
     */
    public void setSelectedGoods(List<SgByChannel> selectedGoods) {
        this.selectedGoods.clear();
        this.selectedGoods.addAll(selectedGoods);
        selectedGoodsAdapter.settList(this.selectedGoods);
        selectedGoodsAdapter.notifyDataSetChanged();
    }

    /**
     * 获得已选商品列表数据
     *
     * @return
     */
    public List<SgByChannel> getSelectedGoods() {
        return selectedGoods;
    }

    /**
     * 清空列表
     */
    public void clearList() {
        selectedGoods.clear();
        selectedGoodsAdapter.clear();
    }

    /**
     * 收缩开关
     */
    public void togle() {
        if (isShow) {
            isShow = false;
            setVisibility(GONE);
        } else {
            isShow = true;
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void select(Map<String, SgByChannel> selectMap, int type) {
        if (selectGoodsListener != null) {
            selectGoodsListener.select(selectMap, type);
        }
    }
}
