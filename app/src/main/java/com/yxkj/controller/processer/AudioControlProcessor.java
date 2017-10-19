package com.yxkj.controller.processer;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.tools.ControlAudioManager;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.common.entity.CmdMsg;

import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * 音频处理器
 *
 * @author huyong
 * @since 2017/9/23
 */
public class AudioControlProcessor implements IProcessor {

    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg cmdMsg) {
        LogUtil.d("AudioControlProcessor:" + cmdMsg.getContent());
        Map<String, String> contentMap = cmdMsg.getContent();
        ControlAudioManager.newInstance().setVolume(Float.parseFloat(contentMap.get("volume")));
        HttpFactory.updateCmdStatus(cmdMsg.getId(), true);
    }

    @Override
    public boolean validateProcessor(CmdMsg cmdMsg) {
        return cmdMsg.getType() == CommonEnum.CmdType.VOLUME;
    }
}
