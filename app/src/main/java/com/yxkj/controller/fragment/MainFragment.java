package com.yxkj.controller.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.SearchGoodsAdapter;
import com.yxkj.controller.base.BaseFragment;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.InputEndListener;
import com.yxkj.controller.callback.SelectListener;
import com.yxkj.controller.util.TimeCountUtl;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.view.CanclePayView;
import com.yxkj.controller.view.CustomVideoView;
import com.yxkj.controller.view.KeyBoardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页，用户输入购买商品页
 */
public class MainFragment extends BaseFragment implements InputEndListener, SelectListener {
    /*键盘*/
    private KeyBoardView keyboardView;
    /* 底部广告视频*/
    private CustomVideoView downVideoView;
    /*商品列表*/
    private RecyclerView recyclerView;
    /*商品列表适配器*/
    private SearchGoodsAdapter adapter;
    /*商品数据集合*/
    private List<String> goods;
    /*支付布局*/
    private LinearLayout layout_pay;
    /*支付二维码*/
    private ImageView img_code;
    /*取消支付弹窗*/
    private CanclePayView view_cancle_pay;
    /*支付倒计时*/
    private TextView tv_count_down;
    /*取消支付按钮*/
    private Button btn_cancle_pay;
    /*左侧静默广告*/
    private ImageView img_left;
    /*中间静默广告*/
    private ImageView img_center;
    /*右侧静默广告*/
    private ImageView img_right;
    /*清空列表*/
    private RelativeLayout layout_clear;
    /*购买商品总价*/
    private TextView tv_total_price;
    /*立即支付按钮*/
    private TextView tv_pay_immediate;
    /*全部商品*/
    private TextView tv_all;
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

    public void setGoodsAndBetterGoodsListener(AllGoodsAndBetterGoodsListener goodsAndBetterGoodsListener) {
        this.goodsAndBetterGoodsListener = goodsAndBetterGoodsListener;
    }

    @Override
    protected int getResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        keyboardView = findViewByIdNoCast(R.id.keyboardView);
        downVideoView = findViewByIdNoCast(R.id.downVideoView);
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        layout_pay = findViewByIdNoCast(R.id.layout_pay);
        img_code = findViewByIdNoCast(R.id.img_code);
        view_cancle_pay = findViewByIdNoCast(R.id.view_cancle_pay);
        tv_count_down = findViewByIdNoCast(R.id.tv_count_down);
        btn_cancle_pay = findViewByIdNoCast(R.id.btn_cancle_pay);
        img_left = findViewByIdNoCast(R.id.img_left);
        img_center = findViewByIdNoCast(R.id.img_center);
        img_right = findViewByIdNoCast(R.id.img_right);
        layout_clear = findViewByIdNoCast(R.id.layout_clear);
        tv_total_price = findViewByIdNoCast(R.id.tv_total_price);
        tv_pay_immediate = findViewByIdNoCast(R.id.tv_pay_immediate);
        tv_all = findViewByIdNoCast(R.id.tv_all);
        tv_better = findViewByIdNoCast(R.id.tv_better);
        layout_after_pay = findViewByIdNoCast(R.id.layout_after_pay);
        tv_close = findViewByIdNoCast(R.id.tv_close);
    }

    @Override
    protected void initData() {
        payImTimeCount = new TimeCountUtl();
        payTimeCount = new TimeCountUtl();
        closeTimeCount = new TimeCountUtl();
        adapter = new SearchGoodsAdapter(getActivity());
        goods = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        downVideoView.setVideoURI(Uri.parse(getActivity().getExternalFilesDir(null) + File.separator + "news.mp4"));
    }

    @Override
    protected void setEvent() {
        /*设置点击监听*/
        setOnClick(tv_pay_immediate, tv_all, tv_better, btn_cancle_pay, layout_clear);
        /*设置是否取消支付监听*/
        view_cancle_pay.setSelectListener(this);
        /*设置输入结束监听*/
        keyboardView.setListener(this);
        /*视屏播放*/
        downVideoView.setOnPreparedListener((MediaPlayer mp) -> {
            downVideoView.start();
        });
        /*监听视频播放结束*/
        downVideoView.setOnCompletionListener((MediaPlayer mediaPlayer) -> {
            downVideoView.start();   /* 循环播放 */
        });
        /*监听视频播放出错*/
        downVideoView.setOnErrorListener((MediaPlayer mediaPlayer, int what, int extra) -> {
            mediaPlayer.reset();
            ToastUtil.showToast("播放视频出错" + extra);
            return true;//如果设置true就可以防止他弹出错误的提示框！
        });
        /*支付倒计时结束*/
        payTimeCount.setCompleteListener(() -> onSure());
        /*立即支付倒计时结束*/
        payImTimeCount.setCompleteListener(() -> onSure());
         /*关闭支付页面倒计时结束*/
        closeTimeCount.setCompleteListener(() -> onSure());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay_immediate: /*立即支付*/
                tv_pay_immediate.setVisibility(View.GONE);/*隐藏立即支付按钮*/
                keyboardView.setVisibility(View.GONE);/*隐藏键盘*/
                layout_pay.setVisibility(View.VISIBLE)/*显示支付页面*/;
                payTimeCount.countDown(0, 120, tv_count_down, "");/*支付倒计时*/
                payImTimeCount.cancle();
                break;
            case R.id.btn_cancle_pay:/*取消支付*/
                view_cancle_pay.setVisibility(View.VISIBLE);/*显示取消支付弹窗*/
                img_code.setVisibility(View.GONE);/*隐藏二维码*/
                break;
            case R.id.layout_clear:/*清空列表*/
                clearList();
                break;
            case R.id.tv_all:/*全部商品*/
                if (goodsAndBetterGoodsListener != null) {
                    goodsAndBetterGoodsListener.onAllGoods();
                }
                break;
            case R.id.tv_better:/*优质商品*/
                if (goodsAndBetterGoodsListener != null) {
                    goodsAndBetterGoodsListener.onBetterGoods();
                }
                break;
        }
    }

    /**
     * 输入结束
     *
     * @param param
     */
    @Override
    public void onEnd(String param) {
        goods.add(param);
        adapter.settList(goods);
        payImTimeCount.cancle();
        /*显示立即支付*/
        payImTimeCount.countDown(0, 60, tv_pay_immediate, "立即支付");
        tv_pay_immediate.setVisibility(View.VISIBLE);
    }

    /**
     * 取消支付,回到初始状态
     */
    @Override
    public void onSure() {
        clearList();
        keyboardView.setVisibility(View.VISIBLE);/*显示键盘*/
        layout_pay.setVisibility(View.GONE)/*隐藏支付页面*/;
        view_cancle_pay.setVisibility(View.GONE);
        img_code.setVisibility(View.VISIBLE);
        keyboardView.clear();
    }

    /**
     * 取消取消支付
     */
    @Override
    public void onCancle() {
        view_cancle_pay.setVisibility(View.GONE);/*隐藏取消弹窗*/
        img_code.setVisibility(View.VISIBLE);/*显示二维码*/
    }

    /**
     * 清空数据列表
     */
    private void clearList() {
        goods.clear();/*清空商品数据*/
        adapter.settList(goods);/*刷新列表*/
        tv_pay_immediate.setVisibility(View.GONE);/*隐藏立即支付按钮*/
    }

}
