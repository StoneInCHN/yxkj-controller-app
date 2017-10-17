package com.yxkj.controller.processer;

import com.yxkj.controller.tools.ControlAudioManager;
import com.yxkj.entity.CmdMsg;

import java.util.Map;

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
        Map<String, String> contentMap = cmdMsg.getContent();
        ControlAudioManager.newInstance().setVolume(Float.parseFloat(contentMap.getOrDefault("volume", "10")));
    }

    @Override
    public boolean validateProcessor(CmdMsg cmdMsg) {
        return cmdMsg.getType() == 3;
    }
}
