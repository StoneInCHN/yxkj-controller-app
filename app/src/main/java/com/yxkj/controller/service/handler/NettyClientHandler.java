package com.yxkj.controller.service.handler;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.service.CustomHeartbeatHandler;
import com.yxkj.controller.util.LogUtil;

import java.util.Date;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/9/13.
 */
public class NettyClientHandler extends CustomHeartbeatHandler {
    private NettyClientBootstrap nettyClientBootstrap;

    public NettyClientHandler(NettyClientBootstrap nettyClientBootstrap) {
        super("Client");
        this.nettyClientBootstrap = nettyClientBootstrap;
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, String msg) {
        new Thread(() -> {
            LogUtil.d("read msg:" + msg);
            String[] msgs = msg.split(";");

            int address = Integer.parseInt(msgs[0]);

            int portId = MyApplication.getMyApplication().getRegisterPort().get(MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressMap().get(address));
            // TODO: 2017/9/29 解析Message
            if (msgs[2].equals("1")) {
                int box = MyApplication.getMyApplication().configBean.getDeviceInfo().getBoxMap().get(Integer.parseInt(msgs[1]));
                String json2 = EVprotocol.EVtrade(portId, 1, address, box, 0);
                LogUtil.d(json2);
            } else if (msgs[2].equals("2")) {
                int box = Integer.parseInt(msgs[1]);
                EVprotocol.EVBentoOpen(portId, address, box);
            }
        }).start();
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

    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        LogUtil.d("Ping time:" + new Date());
        sendPingMsg(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        new Thread(() -> nettyClientBootstrap.startNetty()).start();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LogUtil.e("服務器异常");
    }
}
