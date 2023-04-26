package org.candy.test.queue;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueueRequestTests {

    public static void main(String[] args) {

        KafkaSettings settings = new KafkaSettings();
        QueueProducer<QueueMessage> producerTemplate =
                new KafkaProducerTemplate<>(settings, "request_topic", UUID.randomUUID().toString());
        QueueConsumer<QueueMessage> consumerTemplate =
                new KafkaConsumerTemplate<>(settings, UUID.randomUUID().toString(), "test_group", "response_topic", QueueRequestTests::decode);
        QueueRequestTemplate<QueueMessage, QueueMessage> requestTemplate
                = new DefaultQueueRequestTemplate<>(producerTemplate, consumerTemplate);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String isoDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
                QueueMessage request = new DefaultQueueMessage(UUID.randomUUID(), isoDateTime.getBytes(StandardCharsets.UTF_8));
                Futures.addCallback(requestTemplate.send(request), new FutureCallback<QueueMessage>() {
                    @Override
                    public void onSuccess(QueueMessage msg) {
                        System.out.println("接收消息，key=" + msg.getKey() + ", body=" + new String(msg.getBody()));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                }, MoreExecutors.directExecutor());
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    static QueueMessage decode(ConsumerRecord<UUID, byte[]> record) {
        return new DefaultQueueMessage(record.key(), record.value());
    }
}
