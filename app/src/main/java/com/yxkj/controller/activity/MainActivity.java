package com.yxkj.controller.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseActivity;
import com.yxkj.controller.beans.UrlBean;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.fragment.MainFragment;
import com.yxkj.controller.service.ControllerService;
import com.yxkj.controller.share.SharePrefreceHelper;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.util.VideoDownloadUtil;
import com.yxkj.controller.view.AllGoodsPopupWindow;
import com.yxkj.controller.view.PayPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.yxkj.controller.constant.Constant.IMG_CENTER;
import static com.yxkj.controller.constant.Constant.IMG_LEFT;
import static com.yxkj.controller.constant.Constant.IMG_RIGHT;
import static com.yxkj.controller.constant.Constant.PAYSUCCESS;
import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM;
import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM_DEFAULT_URL;
import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM_NAME;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP_DEFAULT_URL;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP_NAME;


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
    private PayPopupWindow popupWindow;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView() {
        EventBus.getDefault().register(this);
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
        if (!SharePrefreceHelper.getInstence(this).getVideoTopDownloadSuceess()) { //（顶部视频）初次进来下载，如果没有下载过，就下载，
            SharePrefreceHelper.getInstence(this).setVideoTopOnline(true);
            String top_url = SharePrefreceHelper.getInstence(this).getVideoTopUrl();//获取顶部视频url地址
            if (TextUtils.isEmpty(top_url)) {//如果为空，就在线播放并下载
                videoView.setVideoURI(Uri.parse(VIDEO_TOP_DEFAULT_URL));
                SharePrefreceHelper.getInstence(this).setVideoTopUrl(VIDEO_TOP_DEFAULT_URL);
                downloadVideo(VIDEO_TOP_DEFAULT_URL, VIDEO_TOP_NAME, 0);
            } else {
                videoView.setVideoURI(Uri.parse(top_url));
                downloadVideo(top_url, VIDEO_TOP_NAME, 0);
            }
        } else {
            SharePrefreceHelper.getInstence(this).setVideoTopOnline(false);
            setPlayFileVideo(videoView, Constant.VIDEO_TOP_ADDRESS);//已经下载过顶部视频，所以直接通过本地地址播放，
        }
        if (!SharePrefreceHelper.getInstence(this).getVideoBottomDownloadSuceess()) {//（底部视频）初次进来下载，如果没有下载过，就下载，
            String bottom_url = SharePrefreceHelper.getInstence(this).getVideoBottomUrl();//获取底部视频url地址
            SharePrefreceHelper.getInstence(this).setVideoBottomOnline(true);
            if (TextUtils.isEmpty(bottom_url)) {//如果为空，就在线播放并下载
                downVideoView.setVideoURI(Uri.parse(VIDEO_BOTTOM_DEFAULT_URL));
                SharePrefreceHelper.getInstence(this).setVideoBottomUrl(VIDEO_BOTTOM_DEFAULT_URL);
                downloadVideo(VIDEO_BOTTOM_DEFAULT_URL, VIDEO_BOTTOM_NAME, 1);
            } else {
                downVideoView.setVideoURI(Uri.parse(bottom_url));
                downloadVideo(bottom_url, VIDEO_BOTTOM_NAME, 1);
            }
        } else {
            SharePrefreceHelper.getInstence(this).setVideoBottomOnline(false);
            setPlayFileVideo(downVideoView, Constant.VIDEO_BOTTOM_ADDRESS);//已经下载过底部视频，所以直接通过本地地址播放，
        }
        setVideoListener(videoView, 0);
        setVideoListener(downVideoView, 1);
    }

    /**
     * 设置视频监听
     *
     * @param videoView
     */
    private void setVideoListener(IjkVideoView videoView, int type) {
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                switch (type) {
                    case 0:
                        if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoTopDownloadSuceess()
                                && SharePrefreceHelper.getInstence(MainActivity.this).getVideoTopOnline()) { //播放完成，如果顶部视频已经下载成功，就通过本地地址播放
                            SharePrefreceHelper.getInstence(MainActivity.this).setVideoTopOnline(false);
                            setPlayFileVideo(videoView, Constant.VIDEO_TOP_ADDRESS);
                        } else {
                            iMediaPlayer.start();//如果顶部视频没有下载成功，就继续在线播放
                        }
                        break;
                    case 1:
                        if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoBottomDownloadSuceess()
                                && SharePrefreceHelper.getInstence(MainActivity.this).getVideoBottomOnline()) {//播放完成，如果底部视频已经下载成功，就通过本地地址播放
                            SharePrefreceHelper.getInstence(MainActivity.this).setVideoBottomOnline(false);
                            setPlayFileVideo(downVideoView, Constant.VIDEO_BOTTOM_ADDRESS);
                        } else {
                            iMediaPlayer.start();//如果底部视频没有下载成功，就继续在线播放
                        }
                        break;
                }
            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                iMediaPlayer.reset();
                ToastUtil.showToast("播放视频出错" + extra);
                return true;
            }
        });
    }

    /**
     * 设置播放视频
     *
     * @param videoView
     */
    private void setPlayFileVideo(IjkVideoView videoView, String file) {
        videoView.initVideoView(this);
        videoView.setVideoURI(Uri.parse(file));
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
        this.popupWindow = popupWindow;
        popupWindow.showAsDropDown(videoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String response = EVprotocol.EVPortRelease("/dev/ttyS1");
        LogUtil.d("release:" + response);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlBean urlBean) {
        if (urlBean == null)
            return;
        switch (urlBean.key) {
            case VIDEO_TOP:
                //接到上位机指令下载顶部视频，初始化顶部VideoView，在线播放新视频并开始下载顶部新视频
                videoView.initVideoView(this);
                videoView.setVideoURI(Uri.parse(urlBean.url));
                videoView.start();
                SharePrefreceHelper.getInstence(this).setVideoTopOnline(true);
                SharePrefreceHelper.getInstence(this).setVideoTopDownloadSuceess(false);
                SharePrefreceHelper.getInstence(this).setVideoTopUrl(urlBean.url);
                downloadVideo(urlBean.url, VIDEO_TOP_NAME, 0);//下载顶部视频
                break;
            case VIDEO_BOTTOM:
                //接到上位机指令下载底部视频，初始化底部VideoView，在线播放新视频并开始下载顶部新视频
                downVideoView.initVideoView(this);
                downVideoView.setVideoURI(Uri.parse(urlBean.url));
                downVideoView.start();
                SharePrefreceHelper.getInstence(this).setVideoBottomOnline(true);
                SharePrefreceHelper.getInstence(this).setVideoBottomDownloadSuceess(false);
                SharePrefreceHelper.getInstence(this).setVideoBottomUrl(urlBean.url);
                downloadVideo(urlBean.url, VIDEO_BOTTOM_NAME, 1);//下载底部视频
                break;
            case IMG_LEFT:
                //接到上位机指令下载左侧图片
                SharePrefreceHelper.getInstence(this).setImageLeftUrl(urlBean.url);
                mainFragment.setImageLeft(urlBean.url);//左侧图片
                break;
            case IMG_CENTER:
                //接到上位机指令下载中间图片
                SharePrefreceHelper.getInstence(this).setImageCenterUrl(urlBean.url);
                mainFragment.setImageCenter(urlBean.url);//中间图片
                break;
            case IMG_RIGHT:
                //接到上位机指令下载右侧图片
                SharePrefreceHelper.getInstence(this).setImageRightUrl(urlBean.url);
                mainFragment.setImageRight(urlBean.url);//右侧图片
                break;
            case PAYSUCCESS://支付成功
                if (popupWindow != null) {
                    popupWindow.setPaySuccess();
                }
                if (mainFragment != null) {
                    mainFragment.setPaySuccess();
                }
                break;
        }
    }

    /**
     * 下载视频
     * type 0、顶部视频 1、底部视频
     */
    private void downloadVideo(String url, String fileName, int type) {
        VideoDownloadUtil.get().download(url, fileName, type);
    }
}
