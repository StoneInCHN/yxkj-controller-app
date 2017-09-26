package com.yxkj.controller.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseActivity;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.fragment.MainFragment;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.view.AllGoodsPopupWindow;
import com.yxkj.controller.view.CustomVideoView;
import com.yxkj.controller.view.PayPopupWindow;

import java.io.File;


/**
 * 主页
 */
public class MainActivity extends BaseActivity implements AllGoodsAndBetterGoodsListener, ShowPayPopupWindowListener {
    /*轮播广告*/
    private CustomVideoView videoView;
    /*用户输入购买商品页*/
    private MainFragment mainFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        videoView = findViewByIdNoCast(R.id.videoView);
    }

    @Override
    public void initData() {
        initVideo();
        initFragment();
    }

    @Override
    public void setEvent() {
    }


    @Override
    public void onClick(View view) {

    }

    public void initVideo() {
//        DownLoadVideoUtil downLoadVideoUtil = new DownLoadVideoUtil("news.mp4", this);
//        downLoadVideoUtil.setSaveSuceessListener(new DownLoadVideoUtil.SaveSuceessListener() {
//            @Override
//            public void onSuceess(File file) {
//                videoView.setVideoURI(Uri.parse(file.toString()));
////                downVideoView.setVideoURI(Uri.parse(file.toString()));
//
//            }
//        });
//        downLoadVideoUtil.startDownload();
        videoView.setVideoURI(Uri.parse(getExternalFilesDir(null) + File.separator + "news.mp4"));

        videoView.setOnPreparedListener((MediaPlayer mp) -> videoView.start());

        videoView.setOnCompletionListener((MediaPlayer mediaPlayer) -> videoView.start()   /* 循环播放 */);

        videoView.setOnErrorListener((MediaPlayer mediaPlayer, int what, int extra) -> {
            mediaPlayer.reset();
            ToastUtil.showToast("播放视频出错" + extra);
            return true;//如果设置true就可以防止他弹出错误的提示框！
        });
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            addFragment(getSupportFragmentManager().beginTransaction(), mainFragment, "main");
            mainFragment.setGoodsAndBetterGoodsListener(this);
        }
    }

    /**
     * 显示全部商品页
     */
    @Override
    public void onAllGoods() {
        AllGoodsPopupWindow popupWindow = new AllGoodsPopupWindow(this);
        popupWindow.setListener(this);
        popupWindow.showAtLocation(videoView, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示优质商品页
     */
    @Override
    public void onBetterGoods() {
        ToastUtil.showToast("敬请期待");
    }

    /**
     * 添加fragment
     */
    private void addFragment(FragmentTransaction transaction, Fragment fragment, String tag) {
        transaction.add(R.id.layout_fragments, fragment, tag);
        transaction.commit();
    }

    /**
     * 显示支付PopWindow
     *
     * @param popupWindow
     */
    @Override
    public void showPayPopWindow(PayPopupWindow popupWindow) {
        popupWindow.showAtLocation(videoView, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }
}
