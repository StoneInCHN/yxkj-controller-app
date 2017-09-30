package com.yxkj.controller.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.PayGoodsAdapter;
import com.yxkj.controller.callback.BackListener;
import com.yxkj.controller.callback.CompleteListener;
import com.yxkj.controller.callback.SelectListener;
import com.yxkj.controller.util.TimeCountUtl;

import java.util.List;

/**
 * 支付页面
 */

public class PayPopupWindow extends PopupWindow implements CompleteListener {
    /*支付页面*/
    private RelativeLayout layout_pay;
    /*支付成功*/
    private LinearLayout layout_success;
    /*返回首页*/
    private Button btn_back;
    private Context mContext;
    /*已选商品列表*/
    private RecyclerView goodList;
    /*总价*/
    private TextView tv_totalPrice;
    /*支付倒计时*/
    private TextView tv_counter_down;
    /*支付二维码*/
    private ImageView img_pay_code;
    /*适配器*/
    private PayGoodsAdapter payGoodsAdapter;
    /*确认取消支付弹窗*/
    private CanclePayView cancle_pay;
    /*返回监听*/
    private BackListener backListener;
    /*倒计时*/
    private TimeCountUtl timeCountUtl, backTimeUtl;

    public PayPopupWindow(Context context) {
        this(context, null);
    }

    public PayPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        initList();
    }

    /**
     * 初始化View
     */
    private void init() {
        View view = View.inflate(mContext, R.layout.view_pay_popwindow, null);
        goodList = view.findViewById(R.id.goodList);
        layout_pay = view.findViewById(R.id.layout_pay);
        btn_back = view.findViewById(R.id.btn_back);
        layout_success = view.findViewById(R.id.layout_success);
        tv_totalPrice = view.findViewById(R.id.tv_totalPrice);
        tv_counter_down = view.findViewById(R.id.tv_counter_down);
        img_pay_code = view.findViewById(R.id.img_pay_code);
        cancle_pay = view.findViewById(R.id.cancle_pay);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 770);
        setWidth(layoutParams.width);
        setHeight(layoutParams.height);
        setContentView(view);
        tv_counter_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_pay_code.setVisibility(View.GONE);
                tv_counter_down.setVisibility(View.GONE);
                cancle_pay.setVisibility(View.VISIBLE);
            }
        });
        cancle_pay.setSelectListener(new SelectListener() {
            @Override
            public void onSure() {
                if (backListener != null) {
                    cancle();
                    backListener.onBack();
                    dismiss();
                }
            }

            @Override
            public void onCancle() {
                cancle_pay.setVisibility(View.GONE);
                img_pay_code.setVisibility(View.VISIBLE);
                tv_counter_down.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化适配器。倒计时
     */
    private void initList() {
        timeCountUtl = new TimeCountUtl();
        backTimeUtl = new TimeCountUtl();
        payGoodsAdapter = new PayGoodsAdapter(mContext);
        goodList.setLayoutManager(new LinearLayoutManager(mContext));
        goodList.setAdapter(payGoodsAdapter);
        timeCountUtl.countDown(0, 120, tv_counter_down, "取消支付");
        timeCountUtl.setCompleteListener(this);
    }

    /**
     * 设置商品数据
     *
     * @param list
     */
    public void setList(List<String> list) {
        payGoodsAdapter.settList(list);
    }

    public void setBackListener(BackListener completeListener) {
        this.backListener = completeListener;
    }

    @Override
    public void onComplete() {
        if (backListener != null) {
            backListener.onBack();
            dismiss();
        }
    }

    public void cancle() {
        timeCountUtl.cancle();
    }
}
