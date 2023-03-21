package org.candy.test.test.concurrent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * FuturesTests
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/2/15
 */
public class FuturesTests {

    public static void main(String[] args) {

        ListeningExecutorService service = MoreExecutors.newDirectExecutorService();

        ListenableFuture<String> future1 = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello";
            }
        });
        ListenableFuture<String> future2 = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw new RuntimeException("error");
            }
        });
        Futures.addCallback(Futures.successfulAsList(List.of(future1, future2)), new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                System.out.println(Arrays.toString(result.toArray()));
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }
}
