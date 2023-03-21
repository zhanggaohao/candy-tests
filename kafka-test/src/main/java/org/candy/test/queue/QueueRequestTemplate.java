package org.candy.test.queue;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * QueueRequestTemplate
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/2/9
 */
public interface QueueRequestTemplate<Request, Response> {

    ListenableFuture<Response> send(Request request);
}
