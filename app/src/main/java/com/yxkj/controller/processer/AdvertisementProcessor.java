package com.yxkj.controller.processer;

import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ReciveUrlUtil;
import com.yxkj.entity.CmdMsg;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/10/17.
 */

public class AdvertisementProcessor implements IProcessor {
    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg msg) {
        LogUtil.d("AdvertisementProcessor:" + msg.getContent());
        ReciveUrlUtil.newInstance().getJson(msg.getContent());
        HttpFactory.updateCmdStatus(msg.getId(), true);
    }

    @Override
    public boolean validateProcessor(CmdMsg msg) {
        return msg.getType() == 2;
    }
}
