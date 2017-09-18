package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.SelectedGoodsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 已选择商品列表布局
 */

public class SelectedGoodsList extends LinearLayout {
    /*已选择的商品列表*/
    private RecyclerView recyclerView_selected;
    /*已选择商品列表适配器*/
    private SelectedGoodsAdapter selectedGoodsAdapter;
    /*清空列表*/
    private TextView tv_clear;
    /*选择商品列表*/
    private List<String> selectedGoods = new ArrayList<>();
    /*判断是否显示*/
    private boolean isShow;

    public SelectedGoodsList(Context context) {
        this(context, null);
    }

    public SelectedGoodsList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedGoodsList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectedGoodsAdapter = new SelectedGoodsAdapter(getContext());
        init();
        recyclerView_selected.setAdapter(selectedGoodsAdapter);
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_selected_goods, null);
        recyclerView_selected = view.findViewById(R.id.recyclerView_selected);
        tv_clear = view.findViewById(R.id.tv_clear);
        recyclerView_selected.setLayoutManager(new LinearLayoutManager(getContext()));
        addView(view);
    }

    public void setSelectedGoods(List<String> selectedGoods) {
        this.selectedGoods.clear();
        this.selectedGoods.addAll(selectedGoods);
        selectedGoodsAdapter.settList(this.selectedGoods);
    }

    public void clearList() {
        selectedGoods.clear();
        selectedGoodsAdapter.settList(selectedGoods);
    }

    public void togle() {
        if (isShow) {
            isShow = false;
            setVisibility(GONE);
        } else {
            isShow = true;
            setVisibility(VISIBLE);
        }
    }
}
