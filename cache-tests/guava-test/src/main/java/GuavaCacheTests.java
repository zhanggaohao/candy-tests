/*
 * Copyright [yyyy] [name of copyright owner]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GuavaCacheTests
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/10/24
 */
public class GuavaCacheTests {

    @Test
    public void cacheTest() throws ExecutionException, InterruptedException {
        LoadingCache<Object, Object> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(5))
                .refreshAfterWrite(Duration.ofSeconds(3))
                .recordStats()
                .build(new CacheLoader<>() {
                    @Override
                    public Object load(Object key) {
                        System.out.println("loading key:" + key);
                        return "load:" + key;
                    }

                    @Override
                    public ListenableFuture<Object> reload(Object key, Object oldValue) throws Exception {
                        System.out.println("reloading, key:" + key + ",oldValue:" + oldValue);
                        return Futures.immediateFuture("reload:" + load(key));
                    }
                });
        System.out.println(cache.get("a"));
        System.out.println(cache.get("a"));

        System.out.println("------------");
        TimeUnit.SECONDS.sleep(3);
        async(() -> cache.get("a"));

        System.out.println("------------");
        TimeUnit.SECONDS.sleep(5);
        System.out.println(cache.get("a"));
        System.out.println(cache.get("a"));

        CacheStats stats = cache.stats();
        System.out.println(stats);
    }

    private void async(SupplierThrowing<Object> supplier) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                try {
                    System.err.println(supplier.get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    interface SupplierThrowing<T> {

        T get() throws Exception;
    }
}
