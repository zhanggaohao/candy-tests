package org.candy.test;

import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * JdkQueueTests
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/24
 */
public class JdkQueueTests {

    @Test
    public void test() {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            while (true) {
                System.out.println("run 1");
                sleep(500);
            }
        });
        service.execute(() -> {
            while (true) {
                System.out.println("run 2");
                sleep(500);
            }
        });
        service.execute(() -> {
            while (true) {
                System.out.println("run 3");
                sleep(500);
            }
        });

        sleep(5000);
    }

    private void testQueue(Queue<Integer> queue) throws InterruptedException {
        System.out.println(queue.offer(1));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(queue.poll());

        System.out.println(queue.offer(2));
        System.out.println(queue.offer(3));

        System.out.println(queue.poll());
    }

    @Test
    public void testSynchronousQueue() throws Exception {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.offer(1);
                System.out.println("offer 1");
                sleep(100);
            }
        }).start();

        while (true)
            System.out.println("take " + queue.take());
    }

    @Test
    public void testLinkedBlockingQueue() throws Exception {
        testQueue(new LinkedBlockingQueue<>());
    }

    private void sleep(long millisSecond) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisSecond);
        } catch (InterruptedException ignored) {
        }
    }
}
