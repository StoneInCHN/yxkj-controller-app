package com.yxkj.controller.view;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.yxkj.controller.beans.Category;
import com.yxkj.controller.beans.SelectGoodsInfo;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.beans.VerifyStock;
import com.yxkj.controller.beans.VerifyStockBody;
import com.yxkj.controller.callback.BackListener;
import com.yxkj.controller.callback.CompleteListener;
import com.yxkj.controller.callback.SelectGoodsListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.tools.EndlessRecyclerOnScrollListener;
import com.yxkj.controller.util.DisplayUtil;
import com.yxkj.controller.util.QRCodeUtil;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.util.TimeCountUtl;
import com.yxkj.controller.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 全部商品PopupWindow
 */

public class AllGoodsPopupWindow extends PopupWindow implements View.OnClickListener, TabLayout.OnTabSelectedListener, BackListener, CompleteListener, SelectGoodsListener {
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
    private TextView tv_total_price;
    private TextView iv_back_main;
    private LinearLayout back_layout;
    private TimeCountUtl timeCountUtl;
    private ShowPayPopupWindowListener listener;
    private RecyclerView current_goods_recycler;
    /*已选商品*/
    private List<SgByChannel> listSelectedGoods = new ArrayList<>();
    /*商品列表适配器*/
    private CurrentPageGoodsAdapter allGoodsAdapter;
    /*全部标签*/
    private List<Category> categories;

    private List<SgByChannel> goodsList = new ArrayList<>();
    private int page = 1;
    private Category category;
    private SelectGoodsInfo selectGoodsInfos;
    private boolean complete;

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
        tv_total_price = view.findViewById(R.id.tv_total_price);
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
        allGoodsAdapter.setSelectGoodsListener(this);
        seletedGoodsList.setSelectGoodsListener(this);
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
        seletedGoodsList.setSelectGoodsListener(this);
        current_goods_recycler.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadNextPage(View view) {
                if (!complete) {
                    page++;
                    if (category == null) {
                        getByCate("", true, page);
                    } else {
                        getByCate(category.id + "", false, page);
                    }
                }
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        goodsList.clear();
        page = 1;
        if (pos == 0) {
            category = null;
            getByCate("", true, page);
        } else {
            category = categories.get(pos);
            getByCate(category.id + "", false, page);
        }
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
                if (seletedGoodsList.getSelectedGoods().size() == 0) {
                    ToastUtil.showToast("请选择商品后再支付");
                    return;
                }
                verifyStock();
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
     * 已选商品
     */
    @Override
    public void select(Map<String, SgByChannel> selectMap, int type) {
        if (type == 2) {
            allGoodsAdapter.setSelectMap(selectMap);
        }
        Observable.just(selectMap).concatMap(new Function<Map<String, SgByChannel>, ObservableSource<SelectGoodsInfo>>() {
            @Override
            public ObservableSource<SelectGoodsInfo> apply(@NonNull Map<String, SgByChannel> stringByCateMap) throws Exception {
                SelectGoodsInfo s = new SelectGoodsInfo();
                for (Map.Entry<String, SgByChannel> e : stringByCateMap.entrySet()) {
                    SgByChannel b = e.getValue();
                    s.price += b.price * b.number;
                    s.list.add(b);
                }
                return Observable.just(s);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SelectGoodsInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SelectGoodsInfo selectGoodsInfo) {
                selectGoodsInfos = selectGoodsInfo;
                tv_total_price.setText("合计: ￥" + StringUtil.keepNumberSecondCount(selectGoodsInfo.price));
                tv_selected.setText(selectGoodsInfo.list.size() + "");
                seletedGoodsList.setSelectedGoods(selectGoodsInfo.list);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 获取全部商品类别
     */
    private void getCategory() {
        HttpFactory.getCategory(new BaseObserver<List<Category>>() {
            @Override
            protected void onHandleSuccess(List<Category> categorie) {
                Category category = new Category();
                category.cateName = "全部";
                categorie.add(0, category);
                categories = categorie;
                initTabLayout(categorie);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 根据分类查询商品
     */
    private void getByCate(String id, boolean isAll, int page) {
        HttpFactory.getByCate(id, "18", page + "", isAll, new BaseObserver<List<SgByChannel>>() {
            @Override
            protected void onHandleSuccess(List<SgByChannel> byCates) {
                goodsList.addAll(byCates);
                if (byCates.size() < 18) {
                    complete = true;
                }
                allGoodsAdapter.settList(goodsList);
                allGoodsAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 验证商品库存数量请求体
     */
    private void verifyStock() {
        Observable.fromArray(seletedGoodsList.getSelectedGoods()).concatMap(new Function<List<SgByChannel>, ObservableSource<VerifyStockBody>>() {
            @Override
            public ObservableSource<VerifyStockBody> apply(@NonNull List<SgByChannel> sgByChannels) throws Exception {
                VerifyStockBody body = new VerifyStockBody();
                for (SgByChannel sgByChannel : sgByChannels) {
                    body.gList.add(sgByChannel.cId + "-" + sgByChannel.number);
                }
                return Observable.just(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<VerifyStockBody>() {
            @Override
            public void accept(@NonNull VerifyStockBody verifyStockBody) throws Exception {
                verifiedStock(verifyStockBody);
            }
        });
    }

    /**
     * 验证商品库存数量
     */
    private void verifiedStock(VerifyStockBody verifyStockBody) {
        HttpFactory.verifyStock(verifyStockBody, new BaseObserver<List<VerifyStock>>() {
            @Override
            protected void onHandleSuccess(List<VerifyStock> verifyStocks) {
                getQRCodeBitmap(verifyStocks);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }

            @Override
            protected void onHandleStockNotEnough(List<VerifyStock> verifyStocks) {
                Map<String, SgByChannel> map = allGoodsAdapter.getSelectMap();
                for (int i = 0; i < verifyStocks.size(); i++) {
                    VerifyStock v = verifyStocks.get(i);
                    SgByChannel sg = map.get(v.cId + "");
                    if (sg != null) {
                        int sub = sg.number > v.count ? v.count - sg.number : 0;//如果选中数量>库存，则减少数量为选中数量-库存，反之则减少数量为0
                        if (sub < 0)//sub小于0移除该商品
                            map.remove(sg);
                        if (v.count > 0) {//库存量大于0，则添加到新集合
                            sg.number = sg.number > v.count ? v.count : sg.number;//如果选中数量>库存，则选择数量为库存，反之则选中数量不变
                            sg.count = v.count;
                            sg.cId = v.cId;
                        }
                    }
                }
                select(map, 2);
                timeCountUtl.cancle();
                startCountDown();
            }
        });
    }

    /**
     * 拼接二维码url、生成二维码Bitmap
     */
    private void getQRCodeBitmap(List<VerifyStock> verifyStocks) {
        Observable.just(verifyStocks).concatMap(new Function<List<VerifyStock>, ObservableSource<Bitmap>>() {
            @Override
            public ObservableSource<Bitmap> apply(@NonNull List<VerifyStock> verifyStocks) throws Exception {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("http://test.ybjcq.com/h5/cntr/").append(DisplayUtil.getImei()).append("/");
                for (VerifyStock verifyStock : verifyStocks) {
                    stringBuffer.append(verifyStock.cId).append("-").append(verifyStock.count).append(":");
                }
                String url = stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length()).toString();
                return Observable.just(QRCodeUtil.generateBitmap(url, 166, 166));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                timeCountUtl.cancle();
//                PayPopupWindow payPopupWindow = new PayPopupWindow(mContext);
//                payPopupWindow.setList(seletedGoodsList.getSelectedGoods());
//                payPopupWindow.setToatalPrice(selectGoodsInfos.price);
//                payPopupWindow.setPayBitmap(bitmap);
//                payPopupWindow.setBackListener(AllGoodsPopupWindow.this);
                if (listener != null) {
                    listener.showPayPopWindow(seletedGoodsList.getSelectedGoods(), selectGoodsInfos.price, bitmap);
                }
                dismiss();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
