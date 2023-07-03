package org.candy.test.netty.mqtt;

import io.moquette.broker.Server;
import io.moquette.broker.config.*;

import java.io.IOException;

/**
 * MqttServerConfiguration
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/18
 */
public class MqttServerConfiguration {

    private final Server server = new Server();

    public static void main(String[] args) throws IOException {
        MqttServerConfiguration mqttServerConfiguration = new MqttServerConfiguration();

        mqttServerConfiguration.start();
    }

    private IConfig getResourceLoaderConfig() {
        IResourceLoader filesystemLoader = new ClasspathResourceLoader();
        return new ResourceLoaderConfig(filesystemLoader);
    }

    private void start() throws IOException {
        server.startServer(getResourceLoaderConfig());
    }

    private void stop() {
        server.stopServer();
    }
}
