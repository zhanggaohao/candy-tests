package org.candy.test.queue;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

/**
 * DefaultQueueMessage
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public class DefaultQueueMessage implements QueueMessage {

    private final UUID key;

    private MultiValueMap<String, String> headers;

    private final byte[] body;

    public DefaultQueueMessage(UUID key, byte[] body) {
        this(key, new LinkedMultiValueMap<>(10), body);
    }

    public DefaultQueueMessage(UUID key, MultiValueMap<String, String> headers, byte[] body) {
        this.key = key;
        this.headers = headers;
        this.body = body;
    }

    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    public void setHeaders(MultiValueMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public UUID getKey() {
        return key;
    }

    @Override
    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
