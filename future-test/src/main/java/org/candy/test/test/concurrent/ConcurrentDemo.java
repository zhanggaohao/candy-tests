package org.candy.test.test.concurrent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;

public class ConcurrentDemo {

    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        long start = System.currentTimeMillis();
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return "ConcurrentDemo";
            }
        });
//        System.out.println("--");
//        try {
//            System.out.println(future.get());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }

        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void success(String s) {
                System.out.println(s);
                System.out.println(System.currentTimeMillis() - start);
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor());

        ListeningExecutorService decorator = MoreExecutors.listeningDecorator(new ForkJoinPool());
        ListenableFuture<?> submit = decorator.submit(() -> {
        });
        System.out.println(submit);
    }
}
