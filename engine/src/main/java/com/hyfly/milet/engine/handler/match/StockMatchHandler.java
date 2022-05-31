package com.hyfly.milet.engine.handler.match;

import com.gudy.engine.bean.command.CmdResultCode;
import com.gudy.engine.bean.command.RbCmd;
import com.gudy.engine.bean.orderbook.IOrderBook;
import com.gudy.engine.handler.BaseHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

@RequiredArgsConstructor
public class StockMatchHandler extends BaseHandler {

    @NonNull
    private final IntObjectHashMap<IOrderBook> orderBookMap;

    @Override
    public void onEvent(RbCmd cmd, long sequence, boolean endOfBatch) throws Exception {
        if (cmd.resultCode.getCode() < 0) {
            return;//风控未通过
        }

        cmd.resultCode = processCmd(cmd);

    }

    private CmdResultCode processCmd(RbCmd cmd) {
        switch (cmd.command) {
            case NEW_ORDER:
                return orderBookMap.get(cmd.code).newOrder(cmd);
            case CANCEL_ORDER:
                return orderBookMap.get(cmd.code).cancelOrder(cmd);
            case HQ_PUB:
                orderBookMap.forEachKeyValue((code, orderBook) ->
                        cmd.marketDataMap.put(code, orderBook.getL1MarketDataSnapshot())
                );
            default:
                return CmdResultCode.SUCCESS;
        }
    }
}
