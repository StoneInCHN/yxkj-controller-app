package com.yxkj.controller.processer;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.common.entity.CmdMsg;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by huyong on 2017/10/18.
 * 支付处理器
 */

public class PaymentProcessor implements IProcessor{
    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg msg) {
        LogUtil.d("PaymentProcessor:"+msg.toString());
    }

    @Override
    public boolean validateProcessor(CmdMsg msg) {
        return msg.getType()== CommonEnum.CmdType.PAYMENT_SUCCESS;
    }
}
