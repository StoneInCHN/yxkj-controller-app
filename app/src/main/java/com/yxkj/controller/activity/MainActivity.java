package com.yxkj.controller.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseActivity;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.beans.UrlBean;
import com.yxkj.controller.broadcast.NetBroadCastReceiver;
import com.yxkj.controller.callback.AllGoodsAndBetterGoodsListener;
import com.yxkj.controller.callback.NetStateChangeCallback;
import com.yxkj.controller.callback.ShowPayPopupWindowListener;
import com.yxkj.controller.constant.Constant;
import com.yxkj.controller.fragment.MainFragment;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.service.ControllerService;
import com.yxkj.controller.share.SharePrefreceHelper;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.view.AllGoodsPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.yxkj.controller.constant.Constant.IMG_CENTER;
import static com.yxkj.controller.constant.Constant.IMG_LEFT;
import static com.yxkj.controller.constant.Constant.IMG_RIGHT;
import static com.yxkj.controller.constant.Constant.PAYSUCCESS;
import static com.yxkj.controller.constant.Constant.RESTARTSYSTEM;
import static com.yxkj.controller.constant.Constant.VIDEO_BOTTOM;
import static com.yxkj.controller.constant.Constant.VIDEO_TOP;


/**
 * 主页
 */
public class MainActivity extends BaseActivity implements AllGoodsAndBetterGoodsListener, ShowPayPopupWindowListener, NetStateChangeCallback {
    /*轮播广告*/
    private IjkVideoView videoView;
    /*用户输入购买商品页*/
    private MainFragment mainFragment;
    /* 底部广告视频*/
    private IjkVideoView downVideoView;
    protected NetBroadCastReceiver receiver;
    private MainActivityPresenter mainActivityPresenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView() {
        EventBus.getDefault().register(this);
        mainActivityPresenter = new MainActivityPresenterIml();
        long record_id = SharePrefreceHelper.getInstence(this).getRestart();
        if (record_id != 0) {
            HttpFactory.updateCmdStatus(record_id, true);
            SharePrefreceHelper.getInstence(this).setRestart(0);
        }
    }

    @Override
    public void initView() {
        videoView = findViewByIdNoCast(R.id.videoView);
        downVideoView = findViewByIdNoCast(R.id.downVideoView);
    }

    @Override
    public void initData() {
        initFragment();
        Intent intent = new Intent(this, ControllerService.class);
        startService(intent);
        initNetChangeReciever();
    }

    @Override
    public void setEvent() {
        setTopVideoListener();
        setBottVideoListener();
    }


    @Override
    public void onClick(View view) {

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
     */
    @Override
    public void showPayPopWindow(List<SgByChannel> byCateList, double price, Bitmap bitmap) {
        if (mainFragment != null) {
            mainFragment.fromAllGoods(byCateList, price, bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String response = EVprotocol.EVPortRelease("/dev/ttyS1");
        LogUtil.d("release:" + response);
        EventBus.getDefault().unregister(this);
        unRegisterReceiver();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UrlBean urlBean) {
        if (urlBean == null)
            return;
        switch (urlBean.key) {
            case VIDEO_TOP:
                //接到上位机指令下载顶部视频，初始化顶部VideoView，在线播放新视频并开始下载顶部新视频
                mainActivityPresenter.setPlayFileVideo(this, videoView, urlBean.url);
                mainActivityPresenter.orderFromServerDownLoadTopVideo(this, urlBean.url);
                mainActivityPresenter.downloadVideo(urlBean.url, 0);//下载顶部视频
                break;
            case VIDEO_BOTTOM:
                //接到上位机指令下载底部视频，初始化底部VideoView，在线播放新视频并开始下载顶部新视频
                mainActivityPresenter.setPlayFileVideo(this, videoView, urlBean.url);
                mainActivityPresenter.orderFromServerDownLoadBottomVideo(this, urlBean.url);
                mainActivityPresenter.downloadVideo(urlBean.url, 1);//下载底部视频
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
                if (mainFragment != null) {
                    mainFragment.setPaySuccess();
                }
                break;
            case RESTARTSYSTEM://重启系统
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});  //关机
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    /**
     * 初始化网络监听
     */
    protected void initNetChangeReciever() {
        receiver = new NetBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        receiver.setNetStateChangeCallback(this);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(receiver, filter);
    }

    /**
     * 取消注册广播接收者
     */
    protected void unRegisterReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * 网络连接
     */
    @Override
    public void onConnected() {
        mainActivityPresenter.initVideo(this, downVideoView, 1);
        mainActivityPresenter.initVideo(this, videoView, 0);
        mainActivityPresenter.onConnected(this);
    }

    /**
     * 网络断开
     */
    @Override
    public void onDisconnected() {
        mainActivityPresenter.onDisconnected();
    }

    public void setTopVideoListener() {
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });

        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoTopDownloadSuceess()
                        && SharePrefreceHelper.getInstence(MainActivity.this).getVideoTopOnline()) { //播放完成，如果顶部视频已经下载成功，就通过本地地址播放
                    SharePrefreceHelper.getInstence(MainActivity.this).setVideoTopOnline(false);
                    mainActivityPresenter.setPlayFileVideo(MainActivity.this, videoView, Constant.VIDEO_TOP_ADDRESS);
                } else {
                    iMediaPlayer.start();//如果顶部视频没有下载成功，就继续在线播放
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

    public void setBottVideoListener() {
        downVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });
        downVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (SharePrefreceHelper.getInstence(MainActivity.this).getVideoBottomDownloadSuceess()
                        && SharePrefreceHelper.getInstence(MainActivity.this).getVideoBottomOnline()) {//播放完成，如果底部视频已经下载成功，就通过本地地址播放
                    SharePrefreceHelper.getInstence(MainActivity.this).setVideoBottomOnline(false);
                    mainActivityPresenter.setPlayFileVideo(MainActivity.this, downVideoView, Constant.VIDEO_BOTTOM_ADDRESS);
                } else {
                    iMediaPlayer.start();//如果底部视频没有下载成功，就继续在线播放
                }
            }
        });
        downVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                iMediaPlayer.reset();
                ToastUtil.showToast("播放视频出错" + extra);
                return true;
            }
        });
    }
}
