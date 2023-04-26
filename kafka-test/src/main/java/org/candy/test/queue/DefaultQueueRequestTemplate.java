package org.candy.test.queue;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * 基于队列的RequestTemplate
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public class DefaultQueueRequestTemplate<Request extends QueueMessage, Response extends QueueMessage> implements QueueRequestTemplate<Request, Response> {

    private final QueueProducer<Request> queueProducer;
    private final QueueConsumer<Response> queueConsumer;

    private final ConcurrentMap<UUID, SettableFuture<Response>> pendingRequests = new ConcurrentHashMap<>();

    private final ExecutorService executorService;

    private volatile boolean stopped = false;

    public DefaultQueueRequestTemplate(QueueProducer<Request> queueProducer, QueueConsumer<Response> queueConsumer) {
        this.queueProducer = queueProducer;
        this.queueConsumer = queueConsumer;
        this.executorService = Executors.newSingleThreadExecutor();
        init();
    }

    private void init() {
        queueConsumer.subscribe();
        executorService.execute(this::mainLoop);
    }

    private void mainLoop() {
        while (!stopped) {
            stopped = true;
            StopWatch sw = new StopWatch();
            sw.start();
            try {
                fetchAndProcessResponses();
            } catch (Throwable throwable) {
                sw.stop();
                long sleepNanos = TimeUnit.SECONDS.toNanos(25) - sw.getLastTaskTimeNanos();
                LockSupport.parkNanos(sleepNanos);
            }
        }
    }

    private void fetchAndProcessResponses() {
        List<Response> responses = doPoll();
        responses.forEach(this::processResponse);
        queueConsumer.commit();
    }

    private List<Response> doPoll() {
        return queueConsumer.poll(Duration.ofMillis(25));
    }

    private void processResponse(Response response) {
        SettableFuture<Response> future = pendingRequests.remove(response.getKey());
        if (future != null) {
            future.set(response);
        }
    }

    @Override
    public ListenableFuture<Response> send(Request request) {
        MultiValueMap<String, String> headers = request.getHeaders();
        headers.add("requestId", request.getKey().toString());
        headers.add("response_topic", queueConsumer.getTopic());

        SettableFuture<Response> future = SettableFuture.create();
        if (pendingRequests.putIfAbsent(request.getKey(), future) != null) {
            future.setException(new RuntimeException());
            return Futures.immediateFailedFuture(new RuntimeException("Pending request already exists!"));
        }
        sendToRequestTemplate(request, future);
        return future;
    }

    private void sendToRequestTemplate(Request request, SettableFuture<Response> future) {
        // send
        queueProducer.send(queueProducer.getDefaultTopic(), request, new QueueCallback<>() {
            @Override
            public void success(Void result) {

            }

            @Override
            public void failed(Throwable t) {
                pendingRequests.remove(request.getKey());
                future.setException(t);
            }
        });
    }
}
