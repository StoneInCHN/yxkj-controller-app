package com.yxkj.controller.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.AllGoodsPageAdapter;
import com.yxkj.controller.callback.BackListener;
import com.yxkj.controller.callback.GoodsSelectListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 全部商品PopupWindow
 */

public class AllGoodsPopupWindow extends PopupWindow implements View.OnClickListener, TabLayout.OnTabSelectedListener, BackListener, GoodsSelectListener<List<String>> {
    private Context mContext;
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
    /*立即支付*/
    private Button btn_pay;
    private int page = 1;
    private ShowPayPopupWindowListener listener;

    public void setListener(ShowPayPopupWindowListener listener) {
        this.listener = listener;
    }

    private List<Integer> adapterList = new ArrayList<>();

    public AllGoodsPopupWindow(Context context) {
        this(context, null);
    }

    public AllGoodsPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AllGoodsPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        initData();
        setEvent();
    }

    /**
     * 初始化view
     */
    private void init() {
        View view = View.inflate(mContext, R.layout.fragment_allgood, null);
        tabLayout = view.findViewById(R.id.tabLayout);
        next = view.findViewById(R.id.next);
        viewPager = view.findViewById(R.id.viewPager);
        titleBotomView = view.findViewById(R.id.titleBotomView);
        titleView = view.findViewById(R.id.titleView);
        tv_selected = view.findViewById(R.id.tv_selected);
        seletedGoodsList = view.findViewById(R.id.seletedGoodsList);
        btn_pay = view.findViewById(R.id.btn_pay);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2 * (DisplayUtil.getScreenMetrics(mContext).y - DisplayUtil.getNavigationBarHeight(mContext) - DisplayUtil.getStatusBarHeight(mContext)) / 3);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.getScreenMetrics(mContext).y - 605);
        setWidth(layoutParams.width);
        setHeight(layoutParams.height);
        setContentView(view);
        initTabLayout();
    }

    /**
     * 初始化适配器
     */
    private void initData() {
        allGoodsPageAdapter = new AllGoodsPageAdapter(mContext);
        viewPager.setAdapter(allGoodsPageAdapter);
        adapterList.add(page);
        allGoodsPageAdapter.setIntegers(adapterList);
        allGoodsPageAdapter.setGoodsSelectListener(this);
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

    /**
     * 设置监听
     */
    private void setEvent() {
        next.setOnClickListener(this);
        tv_selected.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        /*设置TabLayout切换监听*/
        tabLayout.addOnTabSelectedListener(this);
        /*设置返回监听*/
        titleView.setBackListener(this);
        /*设置返回监听*/
        titleBotomView.setBackListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        page = 1;
        adapterList.clear();
        adapterList.add(page);
        allGoodsPageAdapter.setIntegers(adapterList);
        viewPager.setCurrentItem(0, false);
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
        dismiss();
    }

    /**
     * 已选商品
     *
     * @param goods
     */
    @Override
    public void onGoodsSelect(List<String> goods) {
        tv_selected.setText(goods.size() + "");
        seletedGoodsList.setSelectedGoods(goods);
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
            case R.id.btn_pay:
                titleView.cancle();
                titleBotomView.cancle();
                PayPopupWindow payPopupWindow = new PayPopupWindow(mContext);
                payPopupWindow.setList(seletedGoodsList.getSelectedGoods());
                payPopupWindow.setBackListener(this);
                if (listener != null) {
                    listener.showPayPopWindow(payPopupWindow);
                }
                break;
        }
    }
}
