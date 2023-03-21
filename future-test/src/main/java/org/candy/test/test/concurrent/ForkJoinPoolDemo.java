package org.candy.test.test.concurrent;

import java.util.concurrent.*;

public class ForkJoinPoolDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(123);
            }
        });

        ForkJoinTask<?> submit = pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(223);
            }
        });

        System.out.println(submit.get());

        ForkJoinTask<Object> submit1 = pool.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(333);
                return 444;
            }
        });
        System.out.println(submit1.join());

        System.out.println(submit1.isDone());
        System.out.println(submit1.isCancelled());
        System.out.println(submit1.isCompletedAbnormally());
        System.out.println(submit1.isCompletedNormally());
        System.out.println(submit1.invoke());

        System.out.println(1231);
    }
}
