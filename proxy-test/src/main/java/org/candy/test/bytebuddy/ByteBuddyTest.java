package org.candy.test.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.candy.test.User;
import org.candy.test.UserClient;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * ByteBuddyTest
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class ByteBuddyTest {

    @Test
    public void test0() throws Exception {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(UserClient.class)
                .method(named("getUser"))
                .intercept(FixedValue.value(new User("zcatch")))
                .make()
                .load(ByteBuddyTest.class.getClassLoader())
                .getLoaded();
        UserClient userClient = (UserClient) dynamicType.newInstance();
        System.out.println(userClient.getUser());
    }

    static void redefine() {

        new ByteBuddy()
                .redefine(UserClient.class)
                ;
    }

    public static class Source {
        public String hello(String name) { return null; }
    }

    public static class Target {
        public static String hello(String name) {
            return "Hello " + name + "!";
        }
//        public static String intercept(String name) { return "Hello " + name + "!"; }
//        public static String intercept(int i) { return Integer.toString(i); }
//        public static String intercept(Object o) { return o.toString(); }
    }

    @Test
    public void test1() throws InstantiationException, IllegalAccessException {
        String helloWorld = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .hello("World");
        System.out.println(helloWorld);
    }

    public static class MemoryDatabase {
        public List<String> load(String info) {
            return Arrays.asList(info + ": foo", info + ": bar");
        }
    }

    public static class LoggerInterceptor {

        public static List<String> log(@SuperCall Callable<List<String>> zuper)
                throws Exception {
            System.out.println("Calling database");
            try {
                return zuper.call();
            } finally {
                System.out.println("Returned from database");
            }
        }
    }

    @Test
    public void test2() throws InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<MemoryDatabase> unloaded = new ByteBuddy()
                .subclass(MemoryDatabase.class)
                .method(isDeclaredBy(MemoryDatabase.class)).intercept(MethodDelegation.to(LoggerInterceptor.class))
                .make();
//        unloaded.saveIn(new File("E:\\iotdb"));
        Class<? extends MemoryDatabase> clazz = unloaded
                .load(ByteBuddyTest.class.getClassLoader())
                .getLoaded();

        System.out.println(clazz);

        MemoryDatabase loggingDatabase = clazz.newInstance();

        loggingDatabase.load("abc");
    }
}
