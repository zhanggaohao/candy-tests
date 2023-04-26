package org.candy.test.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * InvokeTarget
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class InvokeTarget {

    private final Object target;

    public InvokeTarget(Object target) {
        this.target = target;
    }

    protected void beforeInvoke() {

    }

    public Object invoke(Method targetMethod, Object[] args) {
        beforeInvoke();
        Object retVal;
        try {
            retVal = targetMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }
}
