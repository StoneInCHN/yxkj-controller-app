package com.yxkj.controller.processer;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.CmdMsg;
import com.yxkj.controller.beans.EV_json;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 出货处理器
 *
 * @author huyong
 * @since 2017/9/23
 */
public class OutBoundProcessor implements IProcessor {

    @Override
    public void process(ChannelHandlerContext ctx, CmdMsg cmdMsg) {

        new Thread(() -> {
            int portId = MyApplication.getMyApplication()
                    .getRegisterPort()
                    .get(MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressMap().get(cmdMsg.getAddress()));

            synchronized (this) {
                String response = null;
                if (cmdMsg.getAddressType() == 1) {
                    //根据货道号获取物理地址
                    int box = MyApplication.getMyApplication().configBean.getDeviceInfo().getBoxMap().get(cmdMsg.getBox());
                    response = EVprotocol.EVtrade(portId, 1, cmdMsg.getAddress(), box, 0);

                } else if (cmdMsg.getAddressType() == 2) {
                    response = EVprotocol.EVBentoOpen(portId, cmdMsg.getAddress(), cmdMsg.getBox());
                }
                EV_json jsonRsp = GsonUtil.getInstance().convertJsonStringToObject(response, EV_json.class);
                LogUtil.d(jsonRsp.toString());
            }
        }).start();
    }

    @Override
    public boolean validateProcessor(CmdMsg cmdMsg) {
        return cmdMsg.getType() == 1;
    }
}
