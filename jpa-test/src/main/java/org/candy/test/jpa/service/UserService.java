package org.candy.test.jpa.service;

import org.candy.test.jpa.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * UserService
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
public interface UserService {

    UserEntity saveAndFlush(UserEntity userEntity);

    List<UserEntity> saveAllAndFlush(Collection<UserEntity> userEntities);

    @Transactional
    List<UserEntity> batchUpdate(Collection<UserEntity> userEntities);
}
