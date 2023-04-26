package org.candy.test.queue;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.UUID;

public interface QueueDecoder<T extends QueueMessage> {

    T decode(ConsumerRecord<UUID, byte[]> record);
}
