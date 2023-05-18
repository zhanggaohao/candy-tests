package org.candy.test.jpa.dao;

import org.candy.test.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * JpaUserDao
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@Component
public class JpaUserDao implements UserDao {

    private final UserRepository userRepository;

    public JpaUserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity saveAndFlush(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public List<UserEntity> saveAllAndFlush(Collection<UserEntity> userEntities) {
        return userRepository.saveAllAndFlush(userEntities);
    }
}
