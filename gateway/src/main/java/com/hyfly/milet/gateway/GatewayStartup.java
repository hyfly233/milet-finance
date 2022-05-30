package com.hyfly.milet.gateway;

import com.hyfly.milet.gateway.checksum.ByteCheckSum;
import com.hyfly.milet.gateway.codec.BodyCodec;
import com.hyfly.milet.gateway.config.GatewayConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
public class GatewayStartup {
    public static void main(String[] args) throws Exception {
        String configFileName = "gateway.xml";

        GatewayConfig config = new GatewayConfig();

        //输入流
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + "\\" + configFileName);
            log.info("gateway.xml exist in jar path");
        } catch (Exception e) {
            inputStream = GatewayStartup.class.getResourceAsStream("/" + configFileName);
            log.info("gateway.xml exist in jar file");
        }

        config.initConfig(inputStream);
//        config.initConfig(GatewayStartup.class.getResource("/").getPath()
//                + configFileName);
        config.setCs(new ByteCheckSum());
        config.setBodyCodec(new BodyCodec());
        config.startup();
    }
}