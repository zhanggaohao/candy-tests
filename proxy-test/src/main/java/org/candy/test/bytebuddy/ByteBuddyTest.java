package org.candy.test.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.candy.test.User;
import org.candy.test.UserClient;

/**
 * ByteBuddyTest
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class ByteBuddyTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(UserClient.class)
                .method(ElementMatchers.named("getUser"))
                .intercept(FixedValue.value(new User("zcatch")))
                .make()
                .load(ByteBuddyTest.class.getClassLoader())
                .getLoaded();
        UserClient userClient = (UserClient) dynamicType.newInstance();
        System.out.println(userClient.getUser());
    }
}
