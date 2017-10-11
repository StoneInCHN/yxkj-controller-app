package com.yxkj.controller.service.handler;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EV_json;
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

    public NettyClientHandler(NettyClientBootstrap nettyClientBootstrap) {
        super("Client");
        this.nettyClientBootstrap = nettyClientBootstrap;
    }

    /**
     * 业务处理
     *
     * @param channelHandlerContext
     * @param msg
     */
    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, String msg) {

        new Thread(() -> {
            // TODO: 2017/9/29 解析Message
            LogUtil.d("read msg:" + msg);
            String[] msgs = msg.split(";");

            int address = Integer.parseInt(msgs[0]);
            int portId = MyApplication.getMyApplication().getRegisterPort().get(MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressMap().get(address));

            synchronized (this) {
                String response = null;
                if (msgs[2].equals("1")) {
                    int box = MyApplication.getMyApplication().configBean.getDeviceInfo().getBoxMap().get(Integer.parseInt(msgs[1]));
                    response = EVprotocol.EVtrade(portId, 1, address, box, 0);

                } else if (msgs[2].equals("2")) {
                    int box = Integer.parseInt(msgs[1]);
                    response = EVprotocol.EVBentoOpen(portId, address, box);
                }
                EV_json jsonRsp = GsonUtil.getInstance().convertJsonStringToObject(response, EV_json.class);
                LogUtil.d(jsonRsp.toString());
            }
        }).start();

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
        String cmd = "1;" + MyApplication.getMyApplication().configBean.getDeviceInfo().getDeviceNo() + ";reg$_$";
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
