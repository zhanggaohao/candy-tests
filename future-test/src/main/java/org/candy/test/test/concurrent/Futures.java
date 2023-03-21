package org.candy.test.test.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class Futures {

//    static <V> void addCallback(ListenableFuture<V> future, FutureCallback<V> callback, Executor executor) {
//        future.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    V v = future.get();
//                    callback.success(v);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } catch (ExecutionException e) {
//                    callback.failed(e);
//                }
//            }
//        }, executor);
//    }

    static <V> void addCallback(Future<V> future, FutureCallback<V> callback, Executor executor) {

//        future.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    V v = future.get();
//                    callback.success(v);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } catch (ExecutionException e) {
//                    callback.failed(e);
//                }
//            }
//        }, executor);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final V v;
                try {
                    v = future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (ExecutionException e) {
                    callback.failed(e.getCause());
                    return;
                }
                callback.success(v);
            }
        });
    }

    interface ListenableFuture {

        void addListener(Runnable runnable, Executor executor);
    }
}
