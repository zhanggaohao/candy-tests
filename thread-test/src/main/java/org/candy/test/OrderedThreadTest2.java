package org.candy.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * OrderedThreadTest
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/28
 */
public class OrderedThreadTest2 {

    @Test
    public void test() throws InterruptedException {
        AtomicBoolean startedA = new AtomicBoolean(true);
        AtomicBoolean startedB = new AtomicBoolean();
        AtomicBoolean startedC = new AtomicBoolean();

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    while (!startedA.get()) {
                        if (startedA.get()) {
                            break;
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] A\n",
                            Thread.currentThread().getName(), i);
                    startedA.set(false);
                    startedB.set(true);
                }
            }
        }, "Thread-A");
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    while (!startedB.get()) {
                        if (startedB.get()) {
                            break;
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] B\n",
                            Thread.currentThread().getName(), i);
                    startedB.set(false);
                    startedC.set(true);
                }
            }
        }, "Thread-B");
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    while (!startedC.get()) {
                        if (startedC.get()) {
                            break;
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] C\n",
                            Thread.currentThread().getName(), i);
                    startedC.set(false);
                    startedA.set(true);
                }
            }
        }, "Thread-C");

        long start = System.currentTimeMillis();
        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
        System.out.println(System.currentTimeMillis() - start);
    }
}
