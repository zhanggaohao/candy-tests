package org.candy.test;

import org.candy.test.jpa.controller.UserController;
import org.candy.test.jpa.entity.UserEntity;
import org.candy.test.jpa.service.UserService;
import org.hibernate.type.SqlTypes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BatchUpdateTests
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@SpringBootTest(classes = SpringJpaApplication.class)
public class BatchUpdateTests {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final static int BATCH_INSERT_ROW_SIZE = 5000;

    @Test
    public void saveAndFlush() {
        long startTime = System.currentTimeMillis();
        try {
            userService.saveAndFlush(UserEntity.ofExample());
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("saveOne, Take {} milliseconds", duration);
        }
    }

    @Test
    public void saveAllAndFlush() {
        long startTime = System.currentTimeMillis();
        try {
            userService.saveAllAndFlush(generateUsers(BATCH_INSERT_ROW_SIZE));
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("saveOne, Take {} milliseconds", duration);
        }
    }

    @Test
    public void saveMultiple() {

        long startTime = System.currentTimeMillis();
        try {
            userService.batchUpdate(generateUsers(BATCH_INSERT_ROW_SIZE));
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("saveMultiple, RowSize：{}, Take {} milliseconds", BATCH_INSERT_ROW_SIZE, duration);
        }
    }

    @Test
    public void saveMultipleForJdbcTemplate() {
        long startTime = System.currentTimeMillis();
        try {
            List<UserEntity> entities = generateUsers(BATCH_INSERT_ROW_SIZE);
            String insertSql = "INSERT INTO c_user (id, created_time, name, age, gender, country, city) VALUES (:id, :createdTime, :name, :age, :gender, :country, :city)";
            SqlParameterSource[] sqlParameterSources = SqlParameterSourceUtils.createBatch(entities);
            for (SqlParameterSource sqlParameterSource : sqlParameterSources) {
                if (sqlParameterSource instanceof BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
                    beanPropertySqlParameterSource.registerSqlType("gender", SqlTypes.VARCHAR);
                    beanPropertySqlParameterSource.registerSqlType("additionalInfo", SqlTypes.VARCHAR);
                }
            }
            jdbcTemplate.batchUpdate(insertSql, sqlParameterSources);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("saveMultipleForJdbcTemplate, RowSize：{}, Take {} milliseconds", BATCH_INSERT_ROW_SIZE, duration);
        }
    }

    private List<UserEntity> generateUsers(int rowSize) {
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < rowSize; i++) {
            userEntities.add(UserEntity.ofExample());
        }
        return userEntities;
    }
}
