package com.yxkj.controller.fragment;


import android.graphics.Bitmap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.SearchGoodsAdapter;
import com.yxkj.controller.base.BaseFragment;
import com.yxkj.controller.base.BaseObserver;
import com.yxkj.controller.beans.GoodsSelectInfo;
import com.yxkj.controller.beans.SearchHandleInfo;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.beans.VerifyStock;
import com.yxkj.controller.beans.VerifyStockBody;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.CompleteListener;
import com.yxkj.controller.callback.InputEndListener;
import com.yxkj.controller.callback.InputManagerPwdListener;
import com.yxkj.controller.callback.SelectListener;
import com.yxkj.controller.callback.ShowInputPwdCallBack;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.util.DisplayUtil;
import com.yxkj.controller.util.GlideUtil;
import com.yxkj.controller.util.QRCodeUtil;
import com.yxkj.controller.util.StringUtil;
import com.yxkj.controller.util.TimeCountUtl;
import com.yxkj.controller.view.AllGoodsPopupWindow;
import com.yxkj.controller.view.CanclePayView;
import com.yxkj.controller.view.InputPwdView;
import com.yxkj.controller.view.KeyBoardView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
 * 主页，用户输入购买商品页
 */
public class MainFragment extends BaseFragment implements InputEndListener<String>, SelectListener, CompleteListener, ShowInputPwdCallBack, InputManagerPwdListener {
    /*键盘*/
    private KeyBoardView keyboardView;
    /*商品列表*/
    private RecyclerView recyclerView;
    /*商品列表适配器*/
    private SearchGoodsAdapter adapter;
    /*商品数据集合*/
    private List<SgByChannel> goods;
    /*支付布局*/
    private LinearLayout layout_pay;
    /*支付二维码*/
    private ImageView img_code;
    /*取消支付弹窗*/
    private CanclePayView view_cancle_pay;
    private RelativeLayout layout_canclePay;
    /*取消支付按钮*/
    private TextView tv_count_down;
    /*左侧静默广告*/
    private ImageView img_left;
    /*中间静默广告*/
    private ImageView img_center;
    /*右侧静默广告*/
    private ImageView img_right;
    /*清空列表*/
    private LinearLayout botom_layout;
    private LinearLayout layout_clear;
    private TextView tv_clear;
    /*购买商品总价*/
    private TextView tv_total_price;
    /*立即支付按钮*/
    private TextView tv_pay_immediate;
    /*全部商品*/
    private ImageView img_all;
    /*优选商品*/
    private TextView tv_better;
    /*支付成功后*/
    private LinearLayout layout_after_pay;
    /*支付成功后关闭按钮*/
    private TextView tv_close;
    /*全部商品或者优质商品监听*/
    private AllGoodsAndBetterGoodsListener goodsAndBetterGoodsListener;
    /*立即支付倒计时*/
    private TimeCountUtl payImTimeCount;
    /*支付倒计时*/
    private TimeCountUtl payTimeCount;
    /*关闭倒计时*/
    private TimeCountUtl closeTimeCount;
    /*灰色总价*/
    private TextView tv_totall_gray;
    /*管理员密码输入弹窗*/
    private InputPwdView input_view;
    /*退出界面（输入管理员密码界面）*/
    private LinearLayout exit_layout;
    /*是否显示支付成功*/
    private boolean isShowSuccess;

    @Override
    public int getResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView(View rootView) {
        keyboardView = findViewByIdNoCast(R.id.keyboardView);
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        layout_pay = findViewByIdNoCast(R.id.layout_pay);
        img_code = findViewByIdNoCast(R.id.img_code);
        view_cancle_pay = findViewByIdNoCast(R.id.view_cancle_pay);
        layout_canclePay = findViewByIdNoCast(R.id.layout_canclePay);
        tv_count_down = findViewByIdNoCast(R.id.tv_count_down);
        img_left = findViewByIdNoCast(R.id.img_left);
        img_center = findViewByIdNoCast(R.id.img_center);
        img_right = findViewByIdNoCast(R.id.img_right);
        botom_layout = findViewByIdNoCast(R.id.botom_layout);
        tv_clear = findViewByIdNoCast(R.id.tv_clear);
        tv_total_price = findViewByIdNoCast(R.id.tv_total_price);
        tv_pay_immediate = findViewByIdNoCast(R.id.tv_pay_immediate);
        img_all = findViewByIdNoCast(R.id.img_all);
        tv_better = findViewByIdNoCast(R.id.tv_better);
        layout_after_pay = findViewByIdNoCast(R.id.layout_after_pay);
        tv_close = findViewByIdNoCast(R.id.tv_close);
        tv_totall_gray = findViewByIdNoCast(R.id.tv_totall_gray);
        layout_clear = findViewByIdNoCast(R.id.layout_clear);
        input_view = findViewByIdNoCast(R.id.input_view);
        exit_layout = findViewByIdNoCast(R.id.exit_layout);
    }

    @Override
    public void initData() {
        payImTimeCount = new TimeCountUtl();
        payTimeCount = new TimeCountUtl();
        closeTimeCount = new TimeCountUtl();
        adapter = new SearchGoodsAdapter(getActivity());
        goods = new ArrayList<>();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEvent() {
        /*设置点击监听*/
        setOnClick(tv_pay_immediate, img_all, tv_better, tv_count_down, tv_clear, tv_close);
        /*设置是否取消支付监听*/
        view_cancle_pay.setSelectListener(this);
        /*设置输入结束监听*/
        keyboardView.setListener(this);
        /*支付倒计时结束*/
        payTimeCount.setCompleteListener(this);
        /*立即支付倒计时结束*/
        payImTimeCount.setCompleteListener(this);
         /*关闭支付页面倒计时结束*/
        closeTimeCount.setCompleteListener(this);
        /*设置输入弹出管理员密码弹窗监听*/
        keyboardView.setShowInputPwdCallBack(this);
        input_view.setInputManagerPwdListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay_immediate: /*立即支付*/
                verifyStock();
                break;
            case R.id.tv_count_down:/*取消支付*/
                layout_canclePay.setVisibility(View.VISIBLE);/*显示取消支付弹窗*/
                break;
            case R.id.tv_clear:/*清空列表*/
                clearList();
                break;
            case R.id.img_all:/*全部商品*/
                AllGoodsPopupWindow allGoodsPopupWindow = new AllGoodsPopupWindow(getActivity());
                allGoodsPopupWindow.startCountDown();
                allGoodsPopupWindow.showAtLocation(img_all, Gravity.CENTER, 0, 0);
                if (goodsAndBetterGoodsListener != null) {
                    goodsAndBetterGoodsListener.onAllGoods(allGoodsPopupWindow);
                }
                break;
            case R.id.tv_better:/*优质商品*/
                if (goodsAndBetterGoodsListener != null) {
                    goodsAndBetterGoodsListener.onBetterGoods();
                }
                break;
            case R.id.tv_close:
                onCancle();
                break;
        }
    }

    /**
     * 设置监听点击了全部商品还是优质商品
     *
     * @param goodsAndBetterGoodsListener
     */
    public void setGoodsAndBetterGoodsListener(AllGoodsAndBetterGoodsListener goodsAndBetterGoodsListener) {
        this.goodsAndBetterGoodsListener = goodsAndBetterGoodsListener;
    }

    /**
     * 输入结束
     *
     * @param param
     */
    @Override
    public void onEnd(String param) {
        /*获取商品*/
        HttpFactory.getSgByChannel("1111111111", param, new BaseObserver<SgByChannel>() {
            @Override
            protected void onHandleSuccess(SgByChannel sgByChannel) {
                if (sgByChannel != null) {
                    handleData(sgByChannel);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                keyboardView.clear();
            }

            @Override
            protected void onHandleStockNotEnough(SgByChannel value) {
                super.onHandleStockNotEnough(value);
                keyboardView.clear();
            }
        });
    }

    /**
     * 处理数据避免重复（用户连续输入两个相同的商品码）
     */
    private void handleData(SgByChannel sgByChannel) {
        Observable.just(sgByChannel).concatMap(new Function<SgByChannel, ObservableSource<SearchHandleInfo>>() {
            @Override
            public ObservableSource<SearchHandleInfo> apply(@NonNull SgByChannel sgByChannel) throws Exception {
                SearchHandleInfo searchHandleInfo = new SearchHandleInfo();
                for (SgByChannel sgByChannel1 : goods) {
                    if (sgByChannel1.cId == sgByChannel.cId) {
                        searchHandleInfo.isContains = true;
                        if (sgByChannel1.number + 1 > sgByChannel1.count) {
                            searchHandleInfo.isCount = true;
                        } else {
                            sgByChannel1.number++;
                        }
                        searchHandleInfo.sgByChannel = sgByChannel1;
                    }
                }
                if (!searchHandleInfo.isContains) {
                    searchHandleInfo.sgByChannel = sgByChannel;
                    goods.add(sgByChannel);
                }
                return Observable.just(searchHandleInfo);
            }
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SearchHandleInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SearchHandleInfo searchHandleInfo) {
                if (!searchHandleInfo.isContains || (searchHandleInfo.isContains && !searchHandleInfo.isCount)) {
                    adapter.settList(goods);
                    adapter.setTotal_price(sgByChannel.price);
                }
                adapter.setEnable(true);
                keyboardView.clear();
                afterInput();
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
     * 输入商品码结束后
     */
    private void afterInput() {
        payImTimeCount.cancle();
        img_all.setVisibility(View.GONE);//隐藏全部商品
        recyclerView.setVisibility(View.VISIBLE);//显示商品列表
        /*显示立即支付*/
        payImTimeCount.countDown(0, 60, tv_pay_immediate, "立即支付(%ds)");
        botom_layout.setVisibility(View.VISIBLE);//显示立即支付
        layout_clear.setVisibility(View.VISIBLE);//显示立即支付
    }

    /**
     * 取消支付,回到初始状态
     */
    @Override
    public void onSure() {
        goods.clear();/*清空商品数据*/
        adapter.settList(goods);/*刷新列表*/
        adapter.clearTotal();/*总价清零*/
        isShowSuccess = false;
        payTimeCount.cancle();
        closeTimeCount.cancle();
        tv_totall_gray.setVisibility(View.GONE);//隐藏灰色总价
        botom_layout.setVisibility(View.GONE);/*隐藏立即支付按钮*/
        layout_canclePay.setVisibility(View.GONE);
        layout_after_pay.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);//隐藏商品列表
        img_all.setVisibility(View.VISIBLE);//显示全部商品
        keyboardView.setVisibility(View.VISIBLE);/*显示键盘*/
        layout_pay.setVisibility(View.GONE)/*隐藏支付页面*/;
        img_code.setVisibility(View.VISIBLE);
        keyboardView.clear();
    }

    /**
     * 取消取消支付
     */
    @Override
    public void onCancle() {
        layout_canclePay.setVisibility(View.GONE);
    }

    /**
     * 清空数据列表
     */
    private void clearList() {
        onSure();
    }

    /**
     * 倒计时结束
     */
    @Override
    public void onComplete() {
        onSure();
    }

    /**
     * 输入密码正常，退出程序
     */
    @Override
    public void onClickSure() {
        getActivity().finish();
    }

    /**
     * 取消管理员密码弹窗
     */
    @Override
    public void onClickCancle() {
        exit_layout.setVisibility(View.GONE);
    }

    /**
     * 显示管理员密码弹窗
     */
    @Override
    public void onShowInputPwd() {
        exit_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左侧图片
     */
    public void setImageLeft(String url) {
        GlideUtil.setImageAdvert(getActivity(), img_left, url, R.mipmap.banner_left);
    }

    /**
     * 设置中间图片
     */
    public void setImageCenter(String url) {
        GlideUtil.setImageAdvert(getActivity(), img_center, url, R.mipmap.banner_center);
    }

    /**
     * 设置右侧图片
     */
    public void setImageRight(String url) {
        GlideUtil.setImageAdvert(getActivity(), img_right, url, R.mipmap.banner_right);
    }

    /**
     * 用户减少商品数量减为0时
     */
    @Subscribe
    public void onEvent(GoodsSelectInfo goodsSelectInfo) {
        if (goodsSelectInfo != null) {
            if (goodsSelectInfo.isClear && goodsSelectInfo.size == 1) {
                clearList();
            } else {
                payImTimeCount.cancle();
                payImTimeCount.countDown(0, 60, tv_pay_immediate, "立即支付(%ds)");
                //  "恭喜您！您的手机跑分为<font color='#F50057'><big><big><big>888888分</big></big></big></font>，已经超过全国<font color='#00E676'><big><big><big>99%</big></big></big></font>的Android手机。";
                String str = "共计: <font color='#FF6C00'><big><big>" + "￥" + StringUtil.keepNumberSecondCount(goodsSelectInfo.total_price) + "</big></big></font>";
                tv_total_price.setText(Html.fromHtml(str));
                String text = "共计: <big><big>" + "￥" + StringUtil.keepNumberSecondCount(goodsSelectInfo.total_price) + "</big></big>";
                tv_totall_gray.setText(Html.fromHtml(text));
            }
        }
    }

    /**
     * 验证商品库存数量请求体
     */
    private void verifyStock() {
        Observable.fromArray(goods).concatMap(new Function<List<SgByChannel>, ObservableSource<VerifyStockBody>>() {
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
     *
     * @param verifyStockBody
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
                List<SgByChannel> newGoods = new ArrayList<SgByChannel>();
                for (int i = 0; i < verifyStocks.size(); i++) {
                    SgByChannel sg = goods.get(i);
                    VerifyStock v = verifyStocks.get(i);
                    int sub = sg.number > v.count ? v.count - sg.number : 0;//如果选中数量>库存，则减少数量为选中数量-库存，反之则减少数量为0
                    if (sub < 0)//sub小于0总价有变化
                        adapter.setTotal_price(sub * sg.price);
                    if (v.count > 0) {//库存量大于0，则添加到新集合
                        sg.number = sg.number > v.count ? v.count : sg.number;//如果选中数量>库存，则选择数量为库存，反之则选中数量不变
                        sg.count = v.count;
                        sg.cId = v.cId;
                        newGoods.add(sg);
                    }
                }
                goods.clear();
                goods.addAll(newGoods);
                adapter.settList(goods);
                adapter.setEnable(true);
                payImTimeCount.cancle();
                payImTimeCount.countDown(0, 60, tv_pay_immediate, "立即支付(%ds)");
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
                return Observable.just(QRCodeUtil.generateBitmap(url, 129, 129));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        img_code.setImageBitmap(bitmap);
                        adapter.setEnable(false);
                        keyboardView.setVisibility(View.GONE);/*隐藏键盘*/
                        layout_pay.setVisibility(View.VISIBLE)/*显示支付页面*/;
                        payTimeCount.countDown(0, 120, tv_count_down, "取消支付(%ds)");/*支付倒计时*/
                        payImTimeCount.cancle();
                        isShowSuccess = true;
                        layout_clear.setVisibility(View.GONE);
                        tv_totall_gray.setVisibility(View.VISIBLE);
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
     * 支付成功后
     */
    public void setPaySuccess() {
        if (isShowSuccess) {
            closeTimeCount.countDown(0, 5, tv_close, "返回首页(%ds)");
            payTimeCount.cancle();
            layout_canclePay.setVisibility(View.GONE);
            layout_pay.setVisibility(View.GONE);
            layout_after_pay.setVisibility(View.VISIBLE);
        }
    }
}
