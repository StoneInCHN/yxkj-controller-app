package com.yxkj.controller.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yxkj.controller.callback.GoodsSelectListener;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.view.CurrentPageGoodsRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 商品页适配器
 */

public class AllGoodsPageAdapter extends PagerAdapter implements GoodsSelectListener<String> {
    private List<CurrentPageGoodsRecyclerView> mListViews;
    private List<Integer> integers;
    private Context context;
    private List<String> goodsList;
    private GoodsSelectListener<List<String>> goodsSelectListener;

    public void setGoodsSelectListener(GoodsSelectListener<List<String>> goodsSelectListener) {
        this.goodsSelectListener = goodsSelectListener;
    }

    public AllGoodsPageAdapter(Context context) {
        this.context = context;
        mListViews = new ArrayList<>();
        goodsList = new ArrayList<>();
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
        Observable.fromIterable(integers).subscribe(integer -> {
            CurrentPageGoodsRecyclerView recyclerView = new CurrentPageGoodsRecyclerView(context);
            recyclerView.setGoodsSelectListener(this);
            mListViews.add(recyclerView);
        });
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mListViews.size() > 0)
            container.removeView(mListViews.get(position));//删除item
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化item
        CurrentPageGoodsRecyclerView recyclerView = null;
        if (mListViews.size() > 0) {
            recyclerView = mListViews.get(position);
            container.addView(recyclerView);//添加item
        }
        return recyclerView;
    }

    @Override
    public int getCount() {
        return mListViews == null ? 0 : mListViews.size();//返回item数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void onGoodsSelect(String goods) {
        goodsList.add(goods);
        LogUtil.e(goodsList.size() + "");
        if (goodsSelectListener != null) {
            goodsSelectListener.onGoodsSelect(goodsList);
        }
    }
}
