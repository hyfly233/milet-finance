package com.hyfly.milet.gateway.handler;

import com.hyfly.milet.gateway.codec.IBodyCodec;
import com.hyfly.milet.gateway.module.CommonMsg;
import com.hyfly.milet.gateway.module.OrderCmdContainer;
import com.hyfly.milet.gateway.module.res.OrderCmd;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MsgHandler implements IMsgHandler {

    private IBodyCodec bodyCodec;

    @Override
    public void onCounterData(CommonMsg msg) {
        OrderCmd orderCmd;

        try {
            orderCmd = bodyCodec.deserialize(msg.getBody(), OrderCmd.class);
            if(log.isDebugEnabled()){
                log.debug("recv cmd: {}",orderCmd);
            }

            if (!OrderCmdContainer.getInstance().cache(orderCmd)) {
                log.error("gateway queue insert fail,queue length:{},order:{}", OrderCmdContainer.getInstance().size(), orderCmd);
            }

        } catch (Exception e) {
            log.error("decode order cmd error", e);
        }
    }
}