package com.hyfly.milet.counter.service.impl;

import com.hyfly.milet.counter.config.CounterConfig;
import com.hyfly.milet.counter.enums.CmdType;
import com.hyfly.milet.counter.enums.OrderDirection;
import com.hyfly.milet.counter.enums.OrderType;
import com.hyfly.milet.counter.module.info.OrderInfo;
import com.hyfly.milet.counter.module.info.PosiInfo;
import com.hyfly.milet.counter.module.info.TradeInfo;
import com.hyfly.milet.counter.module.res.OrderCmd;
import com.hyfly.milet.counter.service.OrderService;
import com.hyfly.milet.counter.util.DbUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class OrderServiceImpl implements OrderService {
    @Override
    public Long getBalance(long uid) {
        return DbUtil.getBalance(uid);
    }

    @Override
    public List<PosiInfo> getPostList(long uid) {
        return DbUtil.getPosiList(uid);
    }

    @Override
    public List<OrderInfo> getOrderList(long uid) {
        return DbUtil.getOrderList(uid);
    }

    @Override
    public List<TradeInfo> getTradeList(long uid) {
        return DbUtil.getTradeList(uid);
    }

    @Autowired
    private CounterConfig config;

//    @Autowired
//    private GatewayConn gatewayConn;
//
//    @Override
//    public boolean sendOrder(long uid, short type, long timestamp, int code,
//                             byte direction, long price, long volume, byte ordertype) {
//        final OrderCmd orderCmd = OrderCmd.builder()
//                .type(CmdType.of(type))
//                .timestamp(timestamp)
//                .mid(config.getId())
//                .uid(uid)
//                .code(code)
//                .direction(OrderDirection.of(direction))
//                .price(price)
//                .volume(volume)
//                .orderType(OrderType.of(ordertype))
//                .build();
//
//        //1.入库
//        int oid = DbUtil.saveOrder(orderCmd);
//        if (oid < 0) {
//            return false;
//        } else {
//            //1.调整资金持仓数据
//            if (orderCmd.direction == OrderDirection.BUY) {
//                //减少资金
//                DbUtil.minusBalance(orderCmd.uid, orderCmd.price * orderCmd.volume);
//            } else if (orderCmd.direction == OrderDirection.SELL) {
//                //减少持仓
//                DbUtil.minusPosi(orderCmd.uid, orderCmd.code, orderCmd.volume, orderCmd.price);
//            } else {
//                log.error("wrong direction[{}],ordercmd:{}", orderCmd.direction, orderCmd);
//                return false;
//            }
//
//            //2.生成全局ID  组装ID long [  柜台ID,  委托ID ]
//            orderCmd.oid = IDConverter.combineInt2Long(config.getId(), oid);
//
//            //保存委托到缓存
//            byte[] serialize = null;
//            try {
//                serialize = config.getBodyCodec().serialize(orderCmd);
//            } catch (Exception e) {
//                log.error(e);
//            }
//            if (serialize == null) {
//                return false;
//            }
//            config.getVertx().eventBus().send(ORDER_DATA_CACHE_ADDR, Buffer.buffer(serialize));
//
//
//            //3.打包委托(ordercmd --> commonmsg -->tcp数据流)
//            // 4.发送数据
//            gatewayConn.sendOrder(orderCmd);
//
//
//            log.info(orderCmd);
//            return true;
//        }
//    }
//
//
//    @Override
//    public boolean cancelOrder(int uid, int counteroid, int code) {
//
//        final OrderCmd orderCmd = OrderCmd.builder()
//                .uid(uid)
//                .code(code)
//                .type(CmdType.CANCEL_ORDER)
//                .oid(IDConverter.combineInt2Long(config.getId(), counteroid))
//                .build();
//
//        log.info("recv cancel order :{}", orderCmd);
//
//        gatewayConn.sendOrder(orderCmd);
//        return true;
//    }
}