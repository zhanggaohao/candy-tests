package org.candy.test.queue;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

/**
 * Kafka 实现的队列消息消费者
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public class KafkaConsumerTemplate<T extends QueueMessage> implements QueueConsumer<T> {

    private final String topic;
    private final KafkaConsumer<UUID, byte[]> kafkaConsumer;
    private Function<ConsumerRecord<UUID, byte[]>, T> decoder;

    public KafkaConsumerTemplate(KafkaSettings settings, String clientId, String groupId, String topic, Function<ConsumerRecord<UUID, byte[]>, T> decoder) {
        Properties props = settings.toConsumerProps();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        if (!ObjectUtils.isEmpty(groupId)) {
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        }
        this.kafkaConsumer = new KafkaConsumer<>(props);
        this.topic = topic;
        this.decoder = decoder;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public List<T> poll(Duration timeout) {
        List<ConsumerRecord<UUID, byte[]>> records = doPoll(timeout);
        return records.stream().map(this::decode).toList();
    }

    private List<ConsumerRecord<UUID, byte[]>> doPoll(Duration timeout) {
        ConsumerRecords<UUID, byte[]> consumerRecords = kafkaConsumer.poll(timeout);
        List<ConsumerRecord<UUID, byte[]>> records = new ArrayList<>();
        consumerRecords.forEach(records::add);
        return records;
    }

    private T decode(ConsumerRecord<UUID, byte[]> record) {
        return decoder.apply(record);
    }

    @Override
    public void subscribe() {
        kafkaConsumer.subscribe(Collections.singleton(topic));
    }

    @Override
    public void commit() {
        kafkaConsumer.commitSync();
    }
}
