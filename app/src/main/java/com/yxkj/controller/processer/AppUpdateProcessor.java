package com.yxkj.controller.processer;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.common.entity.CmdMsg;
import com.yxkj.controller.util.ApkDownloadUtil;

import io.netty.channel.ChannelHandlerContext;

import static com.yxkj.common.commonenum.CommonEnum.CmdType.APP_UPDATE;

/**
 * Created by caiwu on 2017/10/19.
 */

public class AppUpdateProcessor implements IProcessor {
    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg msg) {
        ApkDownloadUtil.get().download(msg.getContent().get(APP_UPDATE.name()));
    }

    @Override
    public boolean validateProcessor(CmdMsg msg) {
        return msg.getType() == APP_UPDATE;
    }
}
