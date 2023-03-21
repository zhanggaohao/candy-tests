package org.candy.test.queue;

/**
 * 队列消息生产者
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public interface QueueProducer<T extends QueueMessage> {

    String getDefaultTopic();

    void send(String topic, T msg, QueueCallback<Void> callback);
}
