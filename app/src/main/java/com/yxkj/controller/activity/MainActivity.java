package com.yxkj.controller.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseActivity;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.fragment.MainFragment;
import com.yxkj.controller.service.ControllerService;
import com.yxkj.controller.share.SharePrefreceHelper;
import com.yxkj.controller.util.DownLoadVideoUtil;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.view.AllGoodsPopupWindow;
import com.yxkj.controller.view.PayPopupWindow;

import java.io.File;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 主页
 */
public class MainActivity extends BaseActivity implements AllGoodsAndBetterGoodsListener, ShowPayPopupWindowListener {
    /*轮播广告*/
    private IjkVideoView videoView;
    /*用户输入购买商品页*/
    private MainFragment mainFragment;
    /* 底部广告视频*/
    private IjkVideoView downVideoView;

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
        downVideoView = findViewByIdNoCast(R.id.downVideoView);
    }

    @Override
    public void initData() {
        initVideo();
        initFragment();
        Intent intent = new Intent(this, ControllerService.class);
        startService(intent);
    }

    @Override
    public void setEvent() {
    }


    @Override
    public void onClick(View view) {

    }

    public void initVideo() {
        boolean isFirst = SharePrefreceHelper.getInstence(this).getFirstBoolean("first", true);
        if (isFirst) {
            DownLoadVideoUtil downLoadVideoUtil = new DownLoadVideoUtil("news.mp4", this);
            downLoadVideoUtil.setSaveSuceessListener(new DownLoadVideoUtil.SaveSuceessListener() {
                @Override
                public void onSuceess(File file) {
                    setPlayFileVideo(videoView);
                    setPlayFileVideo(downVideoView);
                    SharePrefreceHelper.getInstence(MainActivity.this).setFirstBoolean("first", false);
                }
            });
            downLoadVideoUtil.startDownload();
        } else {
            setPlayFileVideo(videoView);
            setPlayFileVideo(downVideoView);
        }
        setVideoListener(videoView);
        setVideoListener(downVideoView);
    }

    /**
     * 设置视频监听
     *
     * @param videoView
     */
    private void setVideoListener(IjkVideoView videoView) {
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
                iMediaPlayer.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();  /* 循环播放 */
            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                iMediaPlayer.reset();
                ToastUtil.showToast("播放视频出错" + extra);
                LogUtil.e("播放视频出错" + extra);
                return true;
            }
        });
    }

    /**
     * 设置播放视频
     *
     * @param videoView
     */
    private void setPlayFileVideo(IjkVideoView videoView) {
        videoView.setVideoURI(Uri.parse(getExternalFilesDir(null) + File.separator + "news.mp4"));
        videoView.start();
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
    public void onAllGoods(AllGoodsPopupWindow popupWindow) {
        popupWindow.setListener(this);
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
        popupWindow.showAsDropDown(videoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String response = EVprotocol.EVPortRelease("/dev/ttyS1");
        LogUtil.d("release:" + response);
    }
}
