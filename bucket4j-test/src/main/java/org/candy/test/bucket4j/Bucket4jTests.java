package org.candy.test.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

/**
 * Bucket4jTests
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/4/26
 */
public class Bucket4jTests {

    public static void main(String[] args) {
        Bandwidth bandwidth = Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(10)));
        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();
        for (int i = 0; i < 11; i++) {
            boolean tried = bucket.tryConsume(1);
            if (tried) {
                System.out.println(i + " : " + true);
            } else {
                System.err.println(i + " : " + false);
            }
        }
    }
}
