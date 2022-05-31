package com.hyfly.milet.engine.bus;

import com.hyfly.milet.engine.module.CommonMsg;

public interface IBusSender {

    void startup();

    void publish(CommonMsg commonMsg);

}
