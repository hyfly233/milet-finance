package com.hyfly.milet.engine.module;

import com.google.common.collect.Lists;
import com.lmax.disruptor.EventFactory;
import io.netty.util.collection.IntObjectHashMap;

public class RbCmdFactory implements EventFactory<RbCmd> {
    @Override
    public RbCmd newInstance() {
        return RbCmd.builder()
                .resultCode(CmdResultCode.SUCCESS)
                .matchEventList(Lists.newArrayList())
                .marketDataMap(new IntObjectHashMap<>())
                .build();
    }
}
