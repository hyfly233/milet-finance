package com.hyfly.milet.gateway.server;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpServer {

    public static void main(String[] args) {

    }

    public void startServer() {
        Vertx vertx = Vertx.vertx();

        NetServer netServer = vertx.createNetServer();

        netServer.connectHandler(new ConnHandler());
        netServer.listen(23301, res -> {
            if (res.succeeded()) {
                log.info("gateway startup success");
            } else {
                log.info("gateway startup fail");
            }
        });
    }

    public static final int PACKET_HEADER_LENGTH = 4;

    private class ConnHandler implements Handler<NetSocket> {
        @Override
        public void handle(NetSocket netSocket) {
            // 自定义解析器
            final RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);
            parser.setOutput(new Handler<Buffer>() {

                int bodyLength = -1;

                @Override
                public void handle(Buffer buffer) {
                    if (bodyLength == -1) {
                        // 读取包头
                        bodyLength = buffer.getInt(0);
                        parser.fixedSizeMode(bodyLength);
                    } else {
                        byte[] bodyBytes = buffer.getBytes();
                        log.info("get msg {}", new String(bodyBytes));

                        // 恢复现场
                        parser.fixedSizeMode(PACKET_HEADER_LENGTH);
                        bodyLength = -1;
                    }
                }
            });

            netSocket.handler(parser);
        }
    }
}
