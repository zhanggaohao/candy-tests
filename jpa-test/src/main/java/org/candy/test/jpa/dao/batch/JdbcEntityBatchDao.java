package org.candy.test.jpa.dao.batch;

import org.candy.test.jpa.entity.base.Entity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JdbcUserBatchDao
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@Component
public class JdbcEntityBatchDao extends JdbcDaoSupport implements EntityBatchDao {

    public JdbcEntityBatchDao(JdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public <T extends Entity<? extends Serializable>> List<T> batchUpdate(Collection<T> entities, BatchSqlParameterResolver<T> factory) {
        List<T> entityList = new ArrayList<>(entities);
        getJdbcTemplate().batchUpdate(factory.getSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                factory.setValue(ps, entityList.get(i));
            }

            @Override
            public int getBatchSize() {
                return entityList.size();
            }
        });
        return entityList;
    }
}
