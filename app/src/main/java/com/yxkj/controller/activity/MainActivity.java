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
import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP;


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
        if (!SharePrefreceHelper.getInstence(this).getVideoTopDownloadSuceess()) {
            videoView.setVideoURI(Uri.parse("http://tb-video.bdstatic.com/tieba-smallvideo-spider/14960611_9fa6dd80cff56ea0e7ed0793d38e135a.mp4"));
            downloadVideo("http://tb-video.bdstatic.com/tieba-smallvideo-spider/14960611_9fa6dd80cff56ea0e7ed0793d38e135a.mp4", "video_top.mp4", 0);
        } else {
            setPlayFileVideo(videoView, Constant.VIDEO_TOP_ADDRESS);
        }
        if (!SharePrefreceHelper.getInstence(this).getVideoBottomDownloadSuceess()) {
            downVideoView.setVideoURI(Uri.parse("http://tb-video.bdstatic.com/tieba-smallvideo-spider/8091147_4234bf66fbb5c72de53be6ce74b87b42.mp4"));
            downloadVideo("http://tb-video.bdstatic.com/tieba-smallvideo-spider/8091147_4234bf66fbb5c72de53be6ce74b87b42.mp4", "video_bottom.mp4", 1);
        } else {
            setPlayFileVideo(downVideoView, Constant.VIDEO_BOTTOM_ADDRESS);
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
                iMediaPlayer.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                switch (type) {
                    case 0:
                        if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoTopDownloadSuceess()) {
                            videoView.setVideoPath(Constant.VIDEO_TOP_ADDRESS);
                            videoView.start();
                        } else {
                            iMediaPlayer.start();
                        }
                        break;
                    case 1:
                        if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoBottomDownloadSuceess()) {
                            videoView.setVideoPath(Constant.VIDEO_BOTTOM_ADDRESS);
                            videoView.start();
                        } else {
                            iMediaPlayer.start();
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
        videoView.setVideoPath(file);
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
        setPlayFileVideo(videoView, Constant.VIDEO_TOP_ADDRESS);
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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlBean urlBean) {
        if (urlBean == null)
            return;
        switch (urlBean.key) {
            case VIDEO_TOP:
                videoView.initVideoView(this);
                videoView.setVideoURI(Uri.parse(urlBean.url));
                videoView.start();
                SharePrefreceHelper.getInstence(this).setVideoTopDownloadSuceess(false);
                SharePrefreceHelper.getInstence(this).setVideoTopUrl(urlBean.url);
                downloadVideo(urlBean.url, "video_top.mp4", 0);//下载顶部视频
                break;
            case VIDEO_BOTTOM:
                downVideoView.initVideoView(this);
                downVideoView.setVideoURI(Uri.parse(urlBean.url));
                downVideoView.start();
                SharePrefreceHelper.getInstence(this).setVideoBottomDownloadSuceess(false);
                SharePrefreceHelper.getInstence(this).setVideoBottomUrl(urlBean.url);
                downloadVideo(urlBean.url, "video_bottom.mp4", 1);//下载底部视频
                break;
            case IMG_LEFT:
                mainFragment.setImageLeft(urlBean.url);//左侧图片
                break;
            case IMG_CENTER:
                mainFragment.setImageCenter(urlBean.url);//中间图片
                break;
            case IMG_RIGHT:
                mainFragment.setImageRight(urlBean.url);//右侧图片
                break;
        }
    }

    /**
     * 下载视频
     */
    private void downloadVideo(String url, String fileName, int type) {
        VideoDownloadUtil.get().download(url, fileName, type);
    }
}
