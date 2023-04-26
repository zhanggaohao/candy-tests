package org.candy.test;

/**
 * DefaultUserClient
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class DefaultUserClient implements UserClient {
    @Override
    public User getUser() {
        return new User("zcatch");
    }
}
