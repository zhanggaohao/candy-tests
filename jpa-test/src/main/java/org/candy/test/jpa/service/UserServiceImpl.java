package org.candy.test.jpa.service;

import org.candy.test.jpa.dao.batch.EntityBatchDao;
import org.candy.test.jpa.dao.UserDao;
import org.candy.test.jpa.dao.batch.BatchSqlParameterResolver;
import org.candy.test.jpa.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * UserServiceImpl
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final EntityBatchDao entityBatchDao;

    public UserServiceImpl(UserDao userDao, EntityBatchDao entityBatchDao) {
        this.userDao = userDao;
        this.entityBatchDao = entityBatchDao;
    }

    @Override
    public UserEntity saveAndFlush(UserEntity userEntity) {
        return userDao.saveAndFlush(userEntity);
    }

    @Override
    public List<UserEntity> saveAllAndFlush(Collection<UserEntity> userEntities) {
        return userDao.saveAllAndFlush(userEntities);
    }

    @Override
    public List<UserEntity> batchUpdate(Collection<UserEntity> userEntities) {
        String sql = "INSERT INTO c_user (id, created_time, name, age, gender, country, city, additional_info) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        BatchSqlParameterResolver<UserEntity> resolver
                = new BatchSqlParameterResolver<>(sql, (preparedStatement, entity) -> {
                    preparedStatement.setObject(1, entity.getId());
                    preparedStatement.setLong(2, entity.getCreatedTime());
                    preparedStatement.setString(3, entity.getName());
                    preparedStatement.setInt(4, entity.getAge());
                    preparedStatement.setString(5, entity.getGender().name());
                    preparedStatement.setString(6, entity.getCountry());
                    preparedStatement.setString(7, entity.getCity());
                    preparedStatement.setString(8, entity.getAdditionalInfo().toString());
                });
        return entityBatchDao.batchUpdate(userEntities, resolver);
    }
}
