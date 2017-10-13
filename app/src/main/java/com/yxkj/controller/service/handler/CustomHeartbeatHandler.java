package com.yxkj.controller.service.handler;

import com.yxkj.controller.util.LogUtil;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<String> {
    public static final String PING_MSG = "PING_MSG";
    public static final String PONG_MSG = "PONG_MSG";
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String msg) throws Exception {
        if (msg.equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (msg.equals(PONG_MSG)){
            LogUtil.d(name + " get pong msg from " + context.channel().remoteAddress());
        } else {
            handleData(context, msg);
        }
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.wrappedBuffer((PING_MSG+"$_$").getBytes()));
        heartbeatCount++;
        LogUtil.d(name + " sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.wrappedBuffer((PONG_MSG+"$_$").getBytes()));
        heartbeatCount++;
        LogUtil.d(name + " sent pong msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, String msg);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
       LogUtil.e("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        LogUtil.e("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        LogUtil.e("---ALL_IDLE---");
    }
}
