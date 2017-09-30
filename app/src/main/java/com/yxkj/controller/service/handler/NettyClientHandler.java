package com.yxkj.controller.service.handler;

import android.util.Log;

import com.easivend.evprotocol.EVprotocol;
import com.google.gson.Gson;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.util.LogUtil;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by huyong on 2017/9/13.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        LogUtil.d("read msg:" + msg);
        String[] msgs = msg.split(";");

        int address = Integer.parseInt(msgs[0]);
        int box = MyApplication.configBean.getDeviceInfo().getAddressMap().get(Integer.parseInt(msgs[1]));
        // TODO: 2017/9/29 解析Message
        String json2 = EVprotocol.EVtrade(0, 1, address, box, 0);
        LogUtil.d(json2);
    }

    /*
     *
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     *
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO: 2017/9/29 获取设备ID
        LogUtil.d("version:" + MyApplication.getMyApplication().configBean.getVersion());
        String cmd = "1;" + MyApplication.getMyApplication().configBean.getDeviceInfo().getDeviceNo() + ";reg\n";
        ctx.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));

    }
}
