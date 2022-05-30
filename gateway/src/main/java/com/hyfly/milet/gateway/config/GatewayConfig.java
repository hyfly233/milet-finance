package com.hyfly.milet.gateway.config;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.hyfly.milet.gateway.checksum.ICheckSum;
import com.hyfly.milet.gateway.codec.IBodyCodec;
import com.hyfly.milet.gateway.handler.ConnHandler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

@Slf4j
@Getter
public class GatewayConfig {

    //网关ID
    private short id;

    //端口
    private int recvPort;

    //排队机通信Provider端口
    private int fetchServPort;


    //TODO 柜台列表 数据库连接

    @Setter
    private IBodyCodec bodyCodec;

    @Setter
    private ICheckSum cs;

    private Vertx vertx = Vertx.vertx();


    public void initConfig(InputStream in) throws Exception {
        // 创建dom4j 解析器
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        Element root = document.getRootElement();

        //1.端口
        id = Short.parseShort(root.element("id").getText());
        recvPort = Integer.parseInt(root.element("recvport").getText());

        fetchServPort = Integer.parseInt(root.element("fetchservport").getText());
        log.info("GateWay ID:{},Port:{},FetchServPort:{}", id, recvPort, fetchServPort);


        //TODO 数据库连接 连接柜台列表

    }

    public void startup() throws Exception {
        //1.启动TCP服务监听
        initRecv();

        //2.排队机交互
//        initFetchServ();

    }


//    private void initFetchServ() {
//        ServerConfig rpcConfig = new ServerConfig()
//                .setPort(fetchServPort)
//                .setProtocol("bolt");
//
//        //implement IFetchService
//        ProviderConfig<IFetchService> providerConfig = new ProviderConfig<IFetchService>()
//                .setInterfaceId(IFetchService.class.getName())
//                .setRef(() -> OrderCmdContainer.getInstance().getAll())
//                .setServer(rpcConfig);
//        providerConfig.export();
//
//        log.info("gateway startup fetchServ success at port : {}", fetchServPort);
//
//    }

    private void initRecv() {
        NetServer server = vertx.createNetServer();
        server.connectHandler(new ConnHandler(this));
        server.listen(recvPort, res -> {
            if (res.succeeded()) {
                log.info("gateway startup success at port : {}", recvPort);
            } else {
                log.error("gateway startup fail");
            }
        });
    }
}