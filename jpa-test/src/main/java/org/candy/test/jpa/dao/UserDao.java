package org.candy.test.jpa.dao;

import org.candy.test.jpa.entity.UserEntity;

import java.util.Collection;
import java.util.List;

/**
 * UserDao
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
public interface UserDao {

    UserEntity saveAndFlush(UserEntity userEntity);

    List<UserEntity> saveAllAndFlush(Collection<UserEntity> userEntities);
}
