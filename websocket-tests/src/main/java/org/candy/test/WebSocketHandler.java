package org.candy.test;

import jakarta.websocket.*;

/**
 * WebSocketHandler
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/4
 */
@ClientEndpoint(configurator = AuthenticationConfigurator.class)
public class WebSocketHandler {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + ": opened!");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(session.getId() + ": " + message);
    }

    @OnMessage
    public void onPong(Session session, PongMessage message) {
        System.out.println(session.getId() + ": pong - " + message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(session.getId() + ": closed!");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println(session.getId() + ": error! " + t.getMessage());
        t.printStackTrace();
    }
}
