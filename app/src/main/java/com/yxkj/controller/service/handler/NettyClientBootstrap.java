package com.yxkj.controller.service.handler;

import com.yxkj.controller.util.LogUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by huyong on 2017/9/13.
 */
public class NettyClientBootstrap {
    private int port = 3331;
    private String host = "192.167.1.59";
    public SocketChannel socketChannel;

    public void startNetty() throws InterruptedException {
        LogUtil.d("Starting Netty client");
        start();
//        if (start()) {
//            LogUtil.d("Connected to Netty server success");
//            ByteBuf bb = Unpooled.wrappedBuffer(("tableIP=asdf".getBytes(CharsetUtil.UTF_8)));
//            socketChannel.writeAndFlush(bb);
//        }
    }

    private Boolean start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new LoggingHandler());
                p.addLast("decoder", new LineBasedFrameDecoder(1024));
                p.addLast(new StringDecoder());
                p.addLast(new StringEncoder());
                p.addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                LogUtil.d("connect server  成功---------");
                return true;
            } else {
                LogUtil.d("connect server  失败---------");
                TimeUnit.SECONDS.sleep(500);
                startNetty();
                return false;
            }
        } catch (Exception e) {
            System.out.println("无法连接----------------");
            //这里最好暂停一下。不然会基本属于毫秒时间内执行很多次。
            //造成重连失败
            TimeUnit.SECONDS.sleep(500);
            startNetty();
            return false;
        }
    }
}
