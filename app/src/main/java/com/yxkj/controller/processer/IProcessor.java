package com.yxkj.controller.processer;

import com.yxkj.controller.beans.CmdMsg;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author huyong
 * @since 2017/9/19
 */
public interface IProcessor {
    void process(ChannelHandlerContext ctx, CmdMsg msg);

    boolean validateProcessor(CmdMsg msg);
}
