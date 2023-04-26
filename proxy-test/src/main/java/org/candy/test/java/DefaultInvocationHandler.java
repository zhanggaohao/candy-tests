package org.candy.test.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * UserInvoke
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class DefaultInvocationHandler implements InvocationHandler {

    private final InvokeTarget target;

    public DefaultInvocationHandler(InvokeTarget target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return target.invoke(method, args);
    }
}
