package com.hyfly.milet.seq;

import com.hyfly.milet.seq.codec.BodyCodec;
import com.hyfly.milet.seq.config.SeqConfig;

public class SeqStartup2 {

    public static void main(String[] args) throws Exception {
        String configName = "seq2.properties";
        new SeqConfig(configName, new BodyCodec()).startup();
    }
}