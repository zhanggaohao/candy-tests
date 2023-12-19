package org.candy.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * OrderedThreadTest
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/28
 */
public class OrderedThreadTest1 {

    @Test
    public void test() throws InterruptedException {

        AtomicBoolean startedA = new AtomicBoolean(true);
        AtomicBoolean startedB = new AtomicBoolean();
        AtomicBoolean startedC = new AtomicBoolean();

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    if (!startedA.get()) {
                        synchronized (startedA) {
                            if (!startedA.get()) {
                                try {
                                    startedA.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] A\n",
                            Thread.currentThread().getName(), i);
                    startedA.set(false);
                    synchronized (startedB) {
                        startedB.set(true);
                        startedB.notify();
                    }
                }
            }
        }, "Thread-A");
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    if (!startedB.get()) {
                        synchronized (startedB) {
                            if (!startedB.get()) {
                                try {
                                    startedB.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] B\n",
                            Thread.currentThread().getName(), i);
                    startedB.set(false);
                    synchronized (startedC) {
                        startedC.set(true);
                        startedC.notify();
                    }
                }
            }
        }, "Thread-B");
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    if (!startedC.get()) {
                        synchronized (startedC) {
                            if (!startedC.get()) {
                                try {
                                    startedC.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    System.out.printf("Thread[name : %s, iteration : %d] C\n",
                            Thread.currentThread().getName(), i);
                    startedC.set(false);
                    synchronized (startedA) {
                        startedA.set(true);
                        startedA.notify();
                    }
                }
            }
        }, "Thread-C");

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
