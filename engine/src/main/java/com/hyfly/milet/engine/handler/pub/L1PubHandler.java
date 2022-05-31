package com.hyfly.milet.engine.handler.pub;

import com.hyfly.milet.engine.config.EngineConfig;
import com.hyfly.milet.engine.enums.CmdType;
import com.hyfly.milet.engine.handler.BaseHandler;
import com.hyfly.milet.engine.module.*;
import com.hyfly.milet.engine.module.orderbook.MatchEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.collections.api.tuple.primitive.ShortObjectPair;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ShortObjectHashMap;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class L1PubHandler extends BaseHandler {

    public static final int HQ_PUB_RATE = 1000;


    @NonNull
    private final ShortObjectHashMap<List<MatchData>> matcherEventMap;

    @NonNull
    private EngineConfig config;


    @Override
    public void onEvent(RbCmd cmd, long sequence, boolean endOfBatch) throws Exception {
        final CmdType cmdType = cmd.command;

        if (cmdType == CmdType.NEW_ORDER || cmdType == CmdType.CANCEL_ORDER) {
            for (MatchEvent e : cmd.matchEventList) {
                matcherEventMap.get(e.mid).add(e.copy());
            }
        } else if (cmdType == CmdType.HQ_PUB) {
            //1.五档行情
            pubMarketData(cmd.marketDataMap);
            //2.给柜台发送MatchData
            pubMatcherData();
        }

    }

    private void pubMatcherData() {
        if (matcherEventMap.size() == 0) {
            return;
        }

        try {
            for (ShortObjectPair<List<MatchData>> s : matcherEventMap.keyValuesView()) {
                if (CollectionUtils.isEmpty(s.getTwo())) {
                    continue;
                }
                byte[] serialize = config.getBodyCodec().serialize(s.getTwo().toArray(new MatchData[0]));
                pubData(serialize, s.getOne(), MsgConstants.MATCH_ORDER_DATA);

                //清空已发送数据
                s.getTwo().clear();

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public static final short HQ_ADDRESS = -1;

    private void pubMarketData(IntObjectHashMap<L1MarketData> marketDataMap) {

        byte[] serialize = null;
        try {
            serialize = config.getBodyCodec().serialize(marketDataMap.values().toArray(new L1MarketData[0]));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (serialize == null) {
            return;
        }

        pubData(serialize, HQ_ADDRESS, MsgConstants.MATCH_HQ_DATA);

    }

    private void pubData(byte[] serialize, short dst, short msgType) {
        CommonMsg msg = new CommonMsg();
        msg.setBodyLength(serialize.length);
        msg.setChecksum(config.getCs().getChecksum(serialize));
        msg.setMsgSrc(config.getId());
        msg.setMsgDst(dst);
        msg.setMsgType(msgType);
        msg.setStatus(MsgConstants.NORMAL);
        msg.setBody(serialize);
        config.getBusSender().publish(msg);
    }
}
