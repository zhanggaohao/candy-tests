# 糖果测试
* Future Test
* Kafka Test

## Future Test
对Future增强，实现异步监听的callback


## Kafka Test
基于Kafka的Request Template模型的设计

QueueRequestTests
```java
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
```

QueueResponseTests
```java
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
```