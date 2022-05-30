package com.hyfly.milet.counter.config;

import com.hyfly.milet.counter.module.CommonMsg;
import com.hyfly.milet.counter.module.MsgConstants;
import com.hyfly.milet.counter.module.res.OrderCmd;
import com.hyfly.milet.counter.tcp.TcpDirectSender;
import com.hyfly.milet.counter.util.Uuid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Log4j2
@Configuration
public class GatewayConn {

    @Autowired
    private CounterConfig config;

    private TcpDirectSender directSender;

    @PostConstruct
    private void init() {
        directSender = new TcpDirectSender(config.getSendIp(), config.getSendPort(), config.getVertx());
        directSender.startup();
    }

    public void sendOrder(OrderCmd orderCmd) {
        byte[] data = null;
        try {
            data = config.getBodyCodec().serialize(orderCmd);
        } catch (Exception e) {
            log.error("encode error for ordercmd:{}", orderCmd, e);
            return;
        }

        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(data.length);
        msg.setChecksum(config.getCs().getChecksum(data));
        msg.setMsgSrc(config.getId());
        msg.setMsgDst(config.getGatewayId());
        msg.setMsgType(MsgConstants.COUNTER_NEW_ORDER);
        msg.setStatus(MsgConstants.NORMAL);
        msg.setMsgNo(Uuid.getInstance().getUUID());
        msg.setBody(data);
        directSender.send(config.getMsgCodec().encodeToBuffer(msg));
    }

}
