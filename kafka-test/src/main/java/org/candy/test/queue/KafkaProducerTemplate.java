package org.candy.test.queue;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

/**
 * Kafka 实现的队列消息生产者
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public class KafkaProducerTemplate<T extends QueueMessage> implements QueueProducer<T> {

    private final String defaultTopic;
    private final AdminClient adminClient;
    private final KafkaProducer<UUID, byte[]> kafkaProducer;

    public KafkaProducerTemplate(KafkaSettings settings, String defaultTopic, String clientId) {
        Properties props = settings.toProducerProps();
        if (!ObjectUtils.isEmpty(clientId)) {
            props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        }
        this.kafkaProducer = new KafkaProducer<>(props);
        this.defaultTopic = defaultTopic;
        this.adminClient = AdminClient.create(settings.toAdminProps());
    }

    @Override
    public String getDefaultTopic() {
        return defaultTopic;
    }

    @Override
    public void send(String topic, T msg, QueueCallback<Void> callback) {
        try {
            createTopicIfNotExist(topic);
            kafkaProducer.send(new ProducerRecord<>(topic, msg.getKey(), msg.getBody()), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        callback.success(null);
                    } else {
                        callback.failed(e);
                    }
                }
            });
        } catch (Throwable e) {
            callback.failed(e);
            throw e;
        }
    }

    private void createTopicIfNotExist(String topic) {
        NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
        adminClient.createTopics(Collections.singleton(newTopic));
    }
}
