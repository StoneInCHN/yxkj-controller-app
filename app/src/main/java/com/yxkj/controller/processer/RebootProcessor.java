package com.yxkj.controller.processer;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.common.entity.CmdMsg;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.controller.util.ReciveUrlUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/10/18.
 * 重启处理器
 */

public class RebootProcessor implements IProcessor {
    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg msg) {
        ReciveUrlUtil.newInstance().reStartSystem(msg.getId());
        LogUtil.d("RebootProcessor:" + msg.toString());
    }

    @Override
    public boolean validateProcessor(CmdMsg msg) {
        return msg.getType() == CommonEnum.CmdType.RE_BOOT;
    }
}
