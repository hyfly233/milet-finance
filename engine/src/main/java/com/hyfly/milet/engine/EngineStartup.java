package com.hyfly.milet.engine;

import com.hyfly.milet.engine.checksum.ByteCheckSum;
import com.hyfly.milet.engine.codec.BodyCodec;
import com.hyfly.milet.engine.codec.MsgCodec;
import com.hyfly.milet.engine.config.EngineConfig;

public class EngineStartup {
    public static void main(String[] args) throws Exception {
        new EngineConfig(
                "engine.properties",
                new BodyCodec(),
                new ByteCheckSum(),
                new MsgCodec()
        ).startup();
    }
}
