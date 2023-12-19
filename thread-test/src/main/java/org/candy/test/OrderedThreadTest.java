package org.candy.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * OrderedThreadTest
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/28
 */
public class OrderedThreadTest {


    final AtomicInteger seq = new AtomicInteger(0);

    final Object lock = new Object();

    @Test
    public void test1() throws InterruptedException {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    synchronized (lock) {
                        while (seq.get() % 3 != 0) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.printf("Thread[name : %s, iteration : %d] A\n",
                                Thread.currentThread().getName(), i);
                        seq.incrementAndGet();
                        lock.notifyAll();
                    }
                }
            }
        }, "Thread-A");

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    synchronized (lock) {
                        while (seq.get() % 3 != 1) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.printf("Thread[name : %s, iteration : %d] B\n",
                                Thread.currentThread().getName(), i);
                        seq.incrementAndGet();
                        lock.notifyAll();
                    }
                }
            }
        }, "Thread-B");

        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    synchronized (lock) {
                        while (seq.get() % 3 != 2) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.printf("Thread[name : %s, iteration : %d] C\n",
                                Thread.currentThread().getName(), i);
                        seq.incrementAndGet();
                        lock.notifyAll();
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

    @Test
    public void test2() throws InterruptedException {
        AtomicBoolean startedA = new AtomicBoolean(true);
        AtomicBoolean startedB = new AtomicBoolean(false);
        AtomicBoolean startedC = new AtomicBoolean(false);

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    synchronized (startedA) {
                        if (!startedA.get()) {
                            try {
                                startedA.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
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
                    synchronized (startedB) {
                        if (!startedB.get()) {
                            try {
                                startedB.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
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
                    synchronized (startedC) {
                        if (!startedC.get()) {
                            try {
                                startedC.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
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
