package org.candy.test.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.candy.test.DefaultUserClient;
import org.candy.test.UserClient;

import java.lang.reflect.Method;

/**
 * CglibTest
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class CglibTest {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(DefaultUserClient.class);

        enhancer.setCallback(new InvocationHandler() {

            protected void afterInvoke() {
                System.out.println("调用之前。");
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                beforeInvoke();
                Object retVal = method.invoke(proxy, args);
                afterInvoke();
                return retVal;
            }

            protected void beforeInvoke() {
                System.out.println("调用之后");
            }
        });

        UserClient userClient = (UserClient) enhancer.create();

        System.out.println(userClient.getUser());
    }
}
