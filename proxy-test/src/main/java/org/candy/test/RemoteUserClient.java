package org.candy.test;

/**
 * RemoteUserClient
 *
 * @author <a href="mailto:zhanggaohao@trgroup.cn">张高豪</a>
 * @since 2023/3/30
 */
public class RemoteUserClient implements UserClient {

    private UserClient userClient = new DefaultUserClient();

    @Override
    public User getUser() {
        return userClient.getUser();
    }
}
