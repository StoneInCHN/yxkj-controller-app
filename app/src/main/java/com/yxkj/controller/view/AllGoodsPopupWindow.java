package com.yxkj.controller.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.CurrentPageGoodsAdapter;
import com.yxkj.controller.base.BaseObserver;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.beans.ByCate;
import com.yxkj.controller.beans.Category;
import com.yxkj.controller.callback.BackListener;
import com.yxkj.controller.callback.CompleteListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.util.TimeCountUtl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 全部商品PopupWindow
 */

public class AllGoodsPopupWindow extends PopupWindow implements View.OnClickListener, TabLayout.OnTabSelectedListener, BackListener, CompleteListener, SelectedGoodsList.ClearListCallBack, BaseRecyclerViewAdapter.OnItemClickListener<String> {
    private Context mContext;
    /*顶部导航栏*/
    private TabLayout tabLayout;
    /*选择的商品数量*/
    private TextView tv_selected;
    /*点击商品购物车*/
    private RelativeLayout rl_selected;
    /*选择商品列表*/
    private SelectedGoodsList seletedGoodsList;
    /*立即支付*/
    private TextView tv_pay;
    private TextView iv_back_main;
    private LinearLayout back_layout;
    private TimeCountUtl timeCountUtl;
    private int page = 1;
    private ShowPayPopupWindowListener listener;
    private RecyclerView current_goods_recycler;
    /*已选商品*/
    private List<String> listSelectedGoods = new ArrayList<>();
    /*商品列表适配器*/
    private CurrentPageGoodsAdapter allGoodsAdapter;

    public void setListener(ShowPayPopupWindowListener listener) {
        this.listener = listener;
    }

    public AllGoodsPopupWindow(Context context) {
        this(context, null);
    }

    public AllGoodsPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AllGoodsPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getCategory();
        getByCate("");
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
        tv_selected = view.findViewById(R.id.tv_selected);
        rl_selected = view.findViewById(R.id.rl_selected);
        seletedGoodsList = view.findViewById(R.id.seletedGoodsList);
        back_layout = view.findViewById(R.id.back_layout);
        current_goods_recycler = view.findViewById(R.id.current_goods_recycler);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 770);
        tv_pay = view.findViewById(R.id.tv_pay);
        iv_back_main = view.findViewById(R.id.iv_back_main);
        setWidth(layoutParams.width);
        setHeight(layoutParams.height);
        setContentView(view);
    }

    /**
     * 初始化适配器
     */
    private void initData() {
        timeCountUtl = new TimeCountUtl();
        allGoodsAdapter = new CurrentPageGoodsAdapter(mContext);
        current_goods_recycler.setLayoutManager(new GridLayoutManager(mContext, 6));
        current_goods_recycler.setAdapter(allGoodsAdapter);
        allGoodsAdapter.setOnItemClickListener(this);
        seletedGoodsList.setClearListCallBack(this);
    }

    /**
     * 初始化TabLayout
     */
    private void initTabLayout(List<Category> tabs) {
        Observable.fromIterable(tabs).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Category>() {
            @Override
            public void accept(@NonNull Category category) throws Exception {
                TabLayout.Tab t = tabLayout.newTab().setText(category.cateName);
                tabLayout.addTab(t);
            }
        });
    }

    /**
     * 设置监听
     */
    private void setEvent() {
        rl_selected.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        /*设置TabLayout切换监听*/
        tabLayout.addOnTabSelectedListener(this);
        timeCountUtl.setCompleteListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_selected:
                seletedGoodsList.togle();
                break;
            case R.id.tv_pay:
                timeCountUtl.cancle();
                PayPopupWindow payPopupWindow = new PayPopupWindow(mContext);
                payPopupWindow.setList(seletedGoodsList.getSelectedGoods());
                payPopupWindow.setBackListener(this);
                if (listener != null) {
                    listener.showPayPopWindow(payPopupWindow);
                }
                break;
            case R.id.back_layout:
                timeCountUtl.cancle();
                dismiss();
                break;
        }
    }

    /**
     * 开启倒计时
     */
    public void startCountDown() {
        timeCountUtl.countDown(0, 120, iv_back_main, "首页(%ds)");
    }

    /**
     * 返回首页
     */
    @Override
    public void onComplete() {
        dismiss();
    }

    /**
     * 清空列表
     */
    @Override
    public void onClear() {
        listSelectedGoods.clear();
        tv_selected.setText(listSelectedGoods.size() + "");
        seletedGoodsList.setSelectedGoods(listSelectedGoods);
    }

    /**
     * 已选商品
     *
     * @param position
     * @param data
     */
    @Override
    public void onItemClick(int position, String data) {
        tv_selected.setText(listSelectedGoods.size() + "");
        listSelectedGoods.add(data);
        seletedGoodsList.setSelectedGoods(listSelectedGoods);
    }

    /**
     * 获取全部商品类别
     */
    private void getCategory() {
        HttpFactory.getCategory(new BaseObserver<List<Category>>() {
            @Override
            protected void onHandleSuccess(List<Category> categories) {
                initTabLayout(categories);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 根据分类查询商品
     */
    private void getByCate(String id) {
        HttpFactory.getByCate("1111111111", id, "", "", new BaseObserver<List<ByCate>>() {
            @Override
            protected void onHandleSuccess(List<ByCate> byCates) {
                allGoodsAdapter.settList(byCates);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
