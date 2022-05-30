package com.hyfly.milet.counter.codec;

import com.hyfly.milet.counter.module.CommonMsg;
import io.vertx.core.buffer.Buffer;

public interface IMsgCodec {

    //TCP <--> CommonMsg
    Buffer encodeToBuffer(CommonMsg msg);

    CommonMsg decodeFromBuffer(Buffer buffer);

}
