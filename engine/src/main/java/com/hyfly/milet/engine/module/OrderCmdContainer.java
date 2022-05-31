package com.hyfly.milet.engine.module;

import com.google.common.collect.Lists;
import com.hyfly.milet.engine.module.res.OrderCmd;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OrderCmdContainer {

    private static final OrderCmdContainer OUR_INSTANCE = new OrderCmdContainer();

    private final BlockingQueue<OrderCmd> queue = new LinkedBlockingDeque<>();

    private OrderCmdContainer() {
    }

    public static OrderCmdContainer getInstance() {
        return OUR_INSTANCE;
    }

    public boolean cache(OrderCmd cmd) {
        return queue.offer(cmd);
    }

    public int size() {
        return queue.size();
    }

    public List<OrderCmd> getAll() {
        List<OrderCmd> msgList = Lists.newArrayList();
        int count = queue.drainTo(msgList);
        if (count == 0) {
            return Collections.emptyList();
        } else {
            return msgList;
        }
    }
}
