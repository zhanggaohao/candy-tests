package org.candy.test;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Main
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/4
 */
public class Main {

    public static void main(String[] args) throws DeploymentException, IOException, InterruptedException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketHandler handler = new WebSocketHandler();

        Session session = container.connectToServer(handler,
                URI.create("ws://127.0.0.1:9503/api/ws/plugins/telemetry"));

        while (session.isOpen()) {
//            session.getAsyncRemote().sendPing(ByteBuffer.allocate(0));
            session.getAsyncRemote().sendPong(ByteBuffer.allocate(0));
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
