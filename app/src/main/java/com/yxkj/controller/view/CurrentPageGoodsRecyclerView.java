package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yxkj.controller.adapter.CurrentPageGoodsAdapter;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.callback.GoodsSelectListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * 商品列表
 */

public class CurrentPageGoodsRecyclerView extends RecyclerView implements BaseRecyclerViewAdapter.OnItemClickListener<String> {
    /*商品列表适配器*/
    private CurrentPageGoodsAdapter allGoodsAdapter;
    /*商品数据*/
    private List<String> goods = new ArrayList<>();

    private GoodsSelectListener<String> goodsSelectListener;

    public CurrentPageGoodsRecyclerView(Context context) {
        this(context, null);
    }

    public CurrentPageGoodsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurrentPageGoodsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setGoodsSelectListener(GoodsSelectListener<String> goodsSelectListener) {
        this.goodsSelectListener = goodsSelectListener;
    }

    private void initData() {
        allGoodsAdapter = new CurrentPageGoodsAdapter(getContext());
        setLayoutManager(new GridLayoutManager(getContext(), 5));
        setAdapter(allGoodsAdapter);
        getAllGoods();
        allGoodsAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取商品
     */
    private void getAllGoods() {
        Observable.range(1, 12).subscribe(integer -> goods.add(integer + ""));
        allGoodsAdapter.settList(goods);
    }

    @Override
    public void onItemClick(int position, String data) {
        if (goodsSelectListener != null) {
            goodsSelectListener.onGoodsSelect(data);
        }
    }
}
