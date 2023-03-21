package org.candy.test.queue;

import java.time.Duration;
import java.util.List;

/**
 * 队列消息消费者
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public interface QueueConsumer<T extends QueueMessage> {

    String getTopic();

    List<T> poll(Duration timeout);

    void subscribe();

    void commit();
}
