package org.candy.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleTest {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup l = MethodHandles.lookup();
        VarHandle RESULT = l.findVarHandle(User.class, "name", String.class);
        MethodHandle getName = l.findGetter(User.class, "name", String.class);
        MethodHandle setName = l.findSetter(User.class, "name", String.class);

        User user = new User();
        RESULT.compareAndSet(user, null, "aaa");
        System.out.println(user);

        RESULT.compareAndSet(user, "aaa", "bbb");
        System.out.println(user);

        setName.invoke(user, "666");
        System.out.println(getName.invoke(user));
    }
}

class User {
    volatile String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}