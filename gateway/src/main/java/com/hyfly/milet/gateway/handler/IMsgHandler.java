package com.hyfly.milet.gateway.handler;

import com.hyfly.milet.gateway.module.CommonMsg;
import io.vertx.core.net.NetSocket;

public interface IMsgHandler {

    default void onConnect(NetSocket socket) {
    }

    default void onDisConnect(NetSocket socket) {
    }

    default void onException(NetSocket socket, Throwable e) {
    }

    void onCounterData(CommonMsg msg);
}
