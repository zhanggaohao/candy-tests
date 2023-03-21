package org.candy.test.queue;

import org.springframework.util.MultiValueMap;

import java.util.UUID;

/**
 * 队列消息接口
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public interface QueueMessage {

    UUID getKey();

    MultiValueMap<String, String> getHeaders();

    byte[] getBody();
}
