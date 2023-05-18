package org.candy.test.jpa.dao;

import org.candy.test.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * UserRepository
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
