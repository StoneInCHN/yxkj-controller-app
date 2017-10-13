package com.yxkj.controller.processer;

import com.easivend.evprotocol.EVprotocol;
import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.beans.EV_json;
import com.yxkj.controller.util.GsonUtil;
import com.yxkj.controller.util.LogUtil;
import com.yxkj.entity.CmdMsg;

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
            Integer physicAddress = MyApplication.getMyApplication().configBean.getDeviceInfo().getAddressPhysicMap().get(cmdMsg.getAddress());
            //如果找不到对应的物理地址则返回
            if (physicAddress == null || physicAddress < 0) return;
            int portId = MyApplication.getMyApplication()
                    .getRegisterPort()
                    .get(MyApplication.getMyApplication().configBean.getDeviceInfo().getSerialPorts().get(physicAddress - 1));
            synchronized (this) {
                String response = null;
                if (cmdMsg.getAddressType() == 0) {
                    //根据货道号获取物理地址
                    int box = MyApplication.getMyApplication().configBean.getDeviceInfo().getBoxMap().get(cmdMsg.getBox());
                    response = EVprotocol.EVtrade(portId, 1, physicAddress, box, 0);

                } else if (cmdMsg.getAddressType() == 1) {
                    response = EVprotocol.EVBentoOpen(portId, physicAddress, cmdMsg.getBox());
                }
                EV_json jsonRsp = GsonUtil.getInstance().convertJsonStringToObject(response, EV_json.class);
                LogUtil.d(jsonRsp.toString());
            }
        }).start();
    }

    @Override
    public boolean validateProcessor(CmdMsg cmdMsg) {
        return cmdMsg.getType() == 0;
    }
}
