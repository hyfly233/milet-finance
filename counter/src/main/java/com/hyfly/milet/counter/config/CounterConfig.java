package com.hyfly.milet.counter.config;

import com.hyfly.milet.counter.checksum.ICheckSum;
import com.hyfly.milet.counter.codec.IBodyCodec;
import com.hyfly.milet.counter.codec.IMsgCodec;
import io.vertx.core.Vertx;
import lombok.Getter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
@Slf4j
public class CounterConfig {

    /////////////////////会员号////////////////////////////////
    @Value("${counter.id}")
    private short id;

    /////////////////////UUID 相关配置////////////////////////////////
    @Value("${counter.dataCenterId}")
    private long dataCenterId;

    @Value("${counter.workerId}")
    private long workerId;
/////////////////////////////////////////////////////

    /////////////////////websocket配置////////////////////////////////
//    @Value("${counter.pubport}")
//    private int pubPort;

    ///////////////////总线配置//////////////////////

    @Value("${counter.subbusip}")
    private String subBusIp;

    @Value("${counter.subbusport}")
    private int subBusPort;

    /////////////////////网关配置////////////////////////////////
    @Value("${counter.sendip}")
    private String sendIp;

    @Value("${counter.sendport}")
    private int sendPort;

    @Value("${counter.gatewayid}")
    private short gatewayId;
/////////////////////////////////////////////////////

    private Vertx vertx = Vertx.vertx();

    /////////////////////编码相关配置////////////////////////////////
//    private ICheckSum cs = new ByteCheckSum();
//
//    private IBodyCodec bodyCodec = new BodyCodec();

    @Value("${counter.checksum}")
    private String checkSumClass;

    @Value("${counter.bodycodec}")
    private String bodyCodecClass;

    @Value("${counter.msgcodec}")
    private String msgCodecClass;


    private ICheckSum cs;

    private IBodyCodec bodyCodec;

    private IMsgCodec msgCodec;

    @PostConstruct
    private void init() {
        Class<?> clz;

        try {
            clz = Class.forName(checkSumClass);
            cs = (ICheckSum) clz.getDeclaredConstructor().newInstance();

            clz = Class.forName(bodyCodecClass);
            bodyCodec = (IBodyCodec) clz.getDeclaredConstructor().newInstance();

            clz = Class.forName(msgCodecClass);
            msgCodec = (IMsgCodec) clz.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            log.error("init config error ", e);
        }


        //初始化总线连接
//        new MqttBusConsumer(subBusIp, subBusPort, String.valueOf(id), msgCodec, cs, vertx).startup();

    }
}
