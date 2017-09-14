package com.yxkj.controller.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.SearchGoodsAdapter;
import com.yxkj.controller.base.BaseFragment;
import com.yxkj.controller.util.ToastUtil;
import com.yxkj.controller.view.CustomVideoView;
import com.yxkj.controller.view.KeyBoardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页，用户输入购买商品页
 */
public class MainFragment extends BaseFragment {
    private KeyBoardView keyboardView;
    private CustomVideoView downVideoView;
    private RecyclerView recyclerView;
    private SearchGoodsAdapter adapter;
    private List<String> goods;

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
    }

    @Override
    protected void initData() {
        adapter = new SearchGoodsAdapter(getActivity());
        goods = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        downVideoView.setVideoURI(Uri.parse(getActivity().getExternalFilesDir(null) + File.separator + "news.mp4"));
    }

    @Override
    protected void setEvent() {
        downVideoView.setOnPreparedListener((MediaPlayer mp) -> {
            downVideoView.start();
        });
        downVideoView.setOnCompletionListener((MediaPlayer mediaPlayer) -> {
            downVideoView.start();   /* 循环播放 */
        });
        downVideoView.setOnErrorListener((MediaPlayer mediaPlayer, int what, int extra) -> {
            mediaPlayer.reset();
            ToastUtil.showToast("播放视频出错" + extra);
            return true;//如果设置true就可以防止他弹出错误的提示框！
        });
    }

    @Override
    public void onClick(View view) {

    }
}
