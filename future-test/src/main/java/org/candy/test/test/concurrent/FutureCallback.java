package org.candy.test.test.concurrent;

public interface FutureCallback<V> {

    void success(V v);

    void failed(Throwable t);
}
