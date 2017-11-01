package com.yxkj.controller.processer;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ReciveUrlUtil;
import com.yxkj.common.entity.CmdMsg;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/10/17.
 * 广告处理器
 */

public class AdvertisementProcessor implements IProcessor {
    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg msg) {
        LogUtil.d("AdvertisementProcessor:" + msg.getContent());
        ReciveUrlUtil.newInstance().getJson(msg.getContent());
        HttpFactory.updateCmdStatus(msg.getId(), true, null);
    }

    @Override
    public boolean validateProcessor(CmdMsg msg) {
        return msg.getType() == CommonEnum.CmdType.AD_UPDATE;
    }
}
