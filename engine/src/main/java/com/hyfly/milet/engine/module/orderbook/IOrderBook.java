package com.hyfly.milet.engine.module.orderbook;

import com.hyfly.milet.engine.module.CmdResultCode;
import com.hyfly.milet.engine.module.L1MarketData;
import com.hyfly.milet.engine.module.RbCmd;

import static com.hyfly.milet.engine.module.L1MarketData.L1_SIZE;

public interface IOrderBook {

    //1.新增委托
    CmdResultCode newOrder(RbCmd cmd);

    //2.撤单
    CmdResultCode cancelOrder(RbCmd cmd);

    //3.查询行情快照
    default L1MarketData getL1MarketDataSnapshot() {
        final int buySize = limitBuyBucketSize(L1_SIZE);
        final int sellSize = limitSellBucketSize(L1_SIZE);
        final L1MarketData data = new L1MarketData(buySize, sellSize);
        fillBuys(buySize, data);
        fillSells(sellSize, data);
        fillCode(data);

        data.timestamp = System.currentTimeMillis();

        return data;
    }

    void fillCode(L1MarketData data);

    void fillSells(int size, L1MarketData data);

    void fillBuys(int size, L1MarketData data);

    int limitBuyBucketSize(int maxSize);

    int limitSellBucketSize(int maxSize);

    //4.TODO 初始化枚举

}
