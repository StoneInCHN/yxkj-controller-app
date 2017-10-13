package com.yxkj.controller.processer;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EV_json;
import com.yxkj.controller.tools.ControlAudioManager;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.entity.CmdMsg;

import io.netty.channel.ChannelHandlerContext;

/**
 * 出货处理器
 *
 * @author huyong
 * @since 2017/9/23
 */
public class AudioControlProcessor implements IProcessor {

    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg cmdMsg) {
        ControlAudioManager.newInstance().setVolume(Float.parseFloat(cmdMsg.getContent()));
    }

    @Override
    public boolean validateProcessor(CmdMsg cmdMsg) {
        return cmdMsg.getType() == 3;
    }
}
