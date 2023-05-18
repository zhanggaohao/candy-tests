package org.candy.test.jpa.controller;

import org.candy.test.jpa.entity.UserEntity;
import org.candy.test.jpa.service.UserService;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * UserController
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@RequestMapping("/user")
@RestController
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserController(UserService userService, NamedParameterJdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/saveOne")
    private UserEntity saveOne() {
        long startTime = System.currentTimeMillis();
        try {
            return userService.saveAndFlush(UserEntity.ofExample());
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("单条保存耗时：{}ms", duration);
        }
    }

    @GetMapping("/saveMultiple")
    private List<UserEntity> saveMultiple(@RequestParam(defaultValue = "1000") int rowSize) {
        long startTime = System.currentTimeMillis();
        try {
            return userService.batchUpdate(generateUsers(rowSize));
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("批量保存，条数：{}，耗时：{}ms", rowSize, duration);
        }
    }

    @GetMapping("/saveMultipleForJdbcTemplate")
    private List<UserEntity> saveMultipleForJdbcTemplate(@RequestParam(defaultValue = "1000") int rowSize) {
        long startTime = System.currentTimeMillis();
        try {
            List<UserEntity> entities = generateUsers(rowSize);
            String insertSql = "INSERT INTO c_user (id, created_time, name, age, gender, country, city) VALUES (:id, :createdTime, :name, :age, :gender, :country, :city)";
            SqlParameterSource[] sqlParameterSources = SqlParameterSourceUtils.createBatch(entities);
            for (SqlParameterSource sqlParameterSource : sqlParameterSources) {
                if (sqlParameterSource instanceof BeanPropertySqlParameterSource beanPropertySqlParameterSource) {
                    beanPropertySqlParameterSource.registerSqlType("gender", SqlTypes.VARCHAR);
                }
            }
            jdbcTemplate.batchUpdate(insertSql, sqlParameterSources);
            return entities;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("批量保存，条数：{}，耗时：{}ms", rowSize, duration);
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
