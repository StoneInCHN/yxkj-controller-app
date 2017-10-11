package com.yxkj.controller.service.handler;

import com.easivend.evprotocol.EVprotocol;
import com.google.gson.Gson;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.CmdMsg;
import com.yxkj.controller.beans.EV_json;
import com.yxkj.controller.beans.NotifyMessage;
import com.yxkj.controller.processer.OutBoundProcessor;
import com.yxkj.controller.processer.ProcessorWatcher;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;

import java.util.Date;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/9/13.
 */
public class NettyClientHandler extends CustomHeartbeatHandler {
    private NettyClientBootstrap nettyClientBootstrap;

    private ProcessorWatcher watcher;

    public NettyClientHandler(NettyClientBootstrap nettyClientBootstrap) {
        super("Client");
        this.nettyClientBootstrap = nettyClientBootstrap;
        watcher = new ProcessorWatcher();
        watcher.addProcessor(new OutBoundProcessor());
    }

    /**
     * 业务处理
     *
     * @param ctx
     * @param msg
     */
    @Override
    protected void handleData(ChannelHandlerContext ctx, String msg) {
        // TODO: 2017/9/29 解析Message
        LogUtil.d("read msg:" + msg);

        CmdMsg cmdMsg = GsonUtil.getInstance().convertJsonStringToObject(msg, CmdMsg.class);
        watcher.process(ctx, cmdMsg);
    }

    /*
     *
     *
     *  连接建立后触发，发送指令，注册设备号格式1;xxx;reg
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO: 2017/9/29 获取设备ID
        LogUtil.d("deviceNO:" + MyApplication.getMyApplication().configBean.getDeviceInfo().getDeviceNo());
        NotifyMessage msg = new NotifyMessage();
        msg.setContent(MyApplication.getMyApplication().configBean.getDeviceInfo().getDeviceNo());
        msg.setMsgType(NotifyMessage.MsgType.REGISTER);
        String cmd = GsonUtil.getInstance().convertObjectToJsonString(msg) + "$_$";
        ctx.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));

    }

    /**
     * 未收到消息时，发送心跳检查包
     *
     * @param ctx
     */
    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        LogUtil.d("Ping time:" + new Date());
        sendPingMsg(ctx);
    }

    /**
     * 连接断开后启动重连机制
     *
     * @param ctx
     * @throws Exception
     */
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
