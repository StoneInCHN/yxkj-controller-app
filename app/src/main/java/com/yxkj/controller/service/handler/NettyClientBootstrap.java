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
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by huyong on 2017/9/13.
 */
public class NettyClientBootstrap {
    private int port = 3331;
    private String host = "10.1.0.143";
    public SocketChannel socketChannel;

    public void startNetty() {
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
                p.addLast(new IdleStateHandler(8, 0, 0));
                p.addLast(new LoggingHandler());
//                p.addLast("decoder", new LineBasedFrameDecoder(1024));
                ByteBuf buf = Unpooled.copiedBuffer("$_$".getBytes());
                p.addLast(new DelimiterBasedFrameDecoder(1024, buf));
                p.addLast(new StringDecoder());
                p.addLast(new StringEncoder());
                p.addLast(new NettyClientHandler(NettyClientBootstrap.this));
            }
        });

        try {
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                LogUtil.d("connect server  成功---------");
            } else {
                LogUtil.d("connect server  失败---------");
                //重新连接服务器
                future.channel().closeFuture();
                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startNetty();
                }).start();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.e(e.getMessage());
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e1) {
                    e.printStackTrace();
                }
                startNetty();
            }).start();
        }
    }
}
