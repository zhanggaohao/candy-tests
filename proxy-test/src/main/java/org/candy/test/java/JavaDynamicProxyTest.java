package org.candy.test.java;

import org.candy.test.RemoteUserClient;
import org.candy.test.UserClient;

import java.lang.reflect.Proxy;

/**
 * UserProxy
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class JavaDynamicProxyTest {

    public static void main(String[] args) {
        RemoteUserClient defaultUserClient = new RemoteUserClient();
        InvokeTarget invokeTarget = new InvokeTarget(defaultUserClient);
        DefaultInvocationHandler invocationHandler = new DefaultInvocationHandler(invokeTarget);
        Class<?>[] interfaces = {UserClient.class};
        ClassLoader classLoader = JavaDynamicProxyTest.class.getClassLoader();
        Object userProxyInstance = Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);

        UserClient userClient = (UserClient) userProxyInstance;

        System.out.println(userClient.getUser());
    }
}
