package com.yxkj.controller.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.AllGoodsPageAdapter;
import com.yxkj.controller.base.BaseFragment;
import com.yxkj.controller.callback.BackListener;
import com.yxkj.controller.callback.GoodsSelectListener;
import com.yxkj.controller.view.SelectedGoodsList;
import com.yxkj.controller.view.TitleView;
import com.yxkj.controller.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 全部商品页面
 */

public class AllGoodsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, BackListener, GoodsSelectListener<List<String>> {
    /*顶底部TitleView*/
    private TitleView titleView, titleBotomView;
    /*顶部导航栏*/
    private TabLayout tabLayout;
    /*导航标签*/
    private String[] tabs = new String[]{"全部", "水饮牛奶", "饼干蛋糕", "美味零食", "香烟"};
    /*翻页*/
    private ImageView next;
    /*商品页列表*/
    private VerticalViewPager viewPager;
    /*商品页适配器*/
    private AllGoodsPageAdapter allGoodsPageAdapter;
    /*选择的商品数量*/
    private TextView tv_selected;
    /*选择商品列表*/
    private SelectedGoodsList seletedGoodsList;
    private int page = 1;

    private List<Integer> adapterList = new ArrayList<>();
    /*返回首页监听*/
    private BackListener backCallBack;

    public void setBackCallBack(BackListener backCallBack) {
        this.backCallBack = backCallBack;
    }

    @Override
    protected int getResource() {
        return R.layout.fragment_allgood;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        tabLayout = findViewByIdNoCast(R.id.tabLayout);
        next = findViewByIdNoCast(R.id.next);
        viewPager = findViewByIdNoCast(R.id.viewPager);
        titleBotomView = findViewByIdNoCast(R.id.titleBotomView);
        titleView = findViewByIdNoCast(R.id.titleView);
        tv_selected = findViewByIdNoCast(R.id.tv_selected);
        seletedGoodsList = findViewByIdNoCast(R.id.seletedGoodsList);
    }

    @Override
    protected void initData() {
        initTabLayout();
        allGoodsPageAdapter = new AllGoodsPageAdapter(getActivity());
        viewPager.setAdapter(allGoodsPageAdapter);
        adapterList.add(page);
        allGoodsPageAdapter.setIntegers(adapterList);
        allGoodsPageAdapter.setGoodsSelectListener(this);

    }

    @Override
    protected void setEvent() {
        setOnClick(next, tv_selected);
        /*设置TabLayout切换监听*/
        tabLayout.addOnTabSelectedListener(this);
        /*设置返回监听*/
        titleView.setBackListener(this);
        /*设置返回监听*/
        titleBotomView.setBackListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                page++;
                adapterList.add(page);
                allGoodsPageAdapter.setIntegers(adapterList);
                viewPager.setCurrentItem(page, true);
                break;
            case R.id.tv_selected:
                seletedGoodsList.togle();
                break;
        }
    }

    /**
     * 初始化TabLayout
     */
    private void initTabLayout() {
        Observable.fromArray(tabs).subscribe(tab -> {
            TabLayout.Tab t = tabLayout.newTab().setText(tab);
            tabLayout.addTab(t);
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        page = 1;
        adapterList.clear();
        adapterList.add(page);
        allGoodsPageAdapter.setIntegers(adapterList);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 返回首页
     */
    @Override
    public void onBack() {
        if (backCallBack != null) {
            backCallBack.onBack();
            titleBotomView.cancle();
            titleView.cancle();
        }
    }

    /**
     * 倒计时重启
     */
    public void restart() {
        titleBotomView.restart();
        titleView.restart();
    }

    @Override
    public void onGoodsSelect(List<String> goods) {
        tv_selected.setText(goods.size() + "");
        seletedGoodsList.setSelectedGoods(goods);
    }
}
