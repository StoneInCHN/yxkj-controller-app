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

import java.io.File;

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
        boolean isFirst = SharePrefreceHelper.getInstence(this).getFirstBoolean("first", true);
        if (isFirst) {
            downloadVideo("http://tb-video.bdstatic.com/tieba-smallvideo-spider/14960611_9fa6dd80cff56ea0e7ed0793d38e135a.mp4", "video_top.mp4", 0);
            downloadVideo("http://tb-video.bdstatic.com/tieba-smallvideo-spider/8091147_4234bf66fbb5c72de53be6ce74b87b42.mp4", "video_bottom.mp4", 1);
        } else {
            setPlayFileVideo(videoView, Constant.VIDEO_TOP_ADDRESS);
            setPlayFileVideo(downVideoView, Constant.VIDEO_BOTTOM_ADDRESS);
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
    private void setPlayFileVideo(IjkVideoView videoView, String file) {
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
                downloadVideo(urlBean.url, "video_top.mp4", 0);//下载顶部视频
                break;
            case VIDEO_BOTTOM:
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
     *
     * @param type 0、顶部视频下载 1、底部视频下载
     */
    private void downloadVideo(String url, String fileName, int type) {
        VideoDownloadUtil.get().download(url, fileName, new VideoDownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                SharePrefreceHelper.getInstence(MainActivity.this).setFirstBoolean("first", false);
                switch (type) {
                    case 0:
                        setPlayFileVideo(videoView, file.getAbsolutePath());
                        break;
                    case 1:
                        setPlayFileVideo(downVideoView, file.getAbsolutePath());
                        break;
                }
            }

            @Override
            public void onDownloading(int progress) {
                LogUtil.e("视频进度------" + progress);
            }

            @Override
            public void onDownloadFailed() {

            }
        });
    }
}
