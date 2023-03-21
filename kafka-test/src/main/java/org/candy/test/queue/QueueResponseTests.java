package org.candy.test.queue;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class QueueResponseTests {

    public static void main(String[] args) {
        KafkaSettings settings = new KafkaSettings();
        KafkaProducerTemplate<QueueMessage> producerTemplate =
                new KafkaProducerTemplate<>(settings, "response_topic", UUID.randomUUID().toString());
        KafkaConsumerTemplate<QueueMessage> consumerTemplate =
                new KafkaConsumerTemplate<>(settings, UUID.randomUUID().toString(), "test_group", "request_topic", QueueRequestTests::decode);
        consumerTemplate.subscribe();
        while (true) {
            List<QueueMessage> messages = consumerTemplate.poll(Duration.ofMillis(25));
            messages.parallelStream().forEach(msg -> {

            });
            messages.forEach(msg -> {
                System.out.println("接收消息: " + new String(msg.getBody()));
                DefaultQueueMessage queueMessage = new DefaultQueueMessage(msg.getKey(), msg.getBody());
                producerTemplate.send(producerTemplate.getDefaultTopic(), queueMessage, new QueueCallback<Void>() {
                    @Override
                    public void success(Void unused) {
                    }

                    @Override
                    public void failed(Throwable t) {

                    }
                });
            });
            consumerTemplate.commit();
        }
    }
}
