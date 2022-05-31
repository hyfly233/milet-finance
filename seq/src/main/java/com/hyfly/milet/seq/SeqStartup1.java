package com.hyfly.milet.seq;

import com.hyfly.milet.seq.codec.BodyCodec;
import com.hyfly.milet.seq.config.SeqConfig;

public class SeqStartup1 {

    public static void main(String[] args) throws Exception {
        String configName = "seq1.properties";
        new SeqConfig(configName, new BodyCodec()).startup();
    }
}
