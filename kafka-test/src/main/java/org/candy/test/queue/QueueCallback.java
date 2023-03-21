package org.candy.test.queue;

/**
 * QueueCallback
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public interface QueueCallback<V> {

    void success(V v);

    void failed(Throwable t);
}
