package org.candy.test.jpa.dao.batch;

import org.candy.test.jpa.entity.base.Entity;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * BatchSqlParameterResolver
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/11
 */
public class BatchSqlParameterResolver<T extends Entity<? extends Serializable>> {

    private final String sql;

    private final PreparedStatementCreator<T> preparedStatementCreator;

    public BatchSqlParameterResolver(String sql, PreparedStatementCreator<T> preparedStatementCreator) {
        this.sql = sql;
        this.preparedStatementCreator = preparedStatementCreator;
    }

    public String getSql() {
        return sql;
    }

    public void setValue(PreparedStatement ps, T entity) throws SQLException {
        preparedStatementCreator.setValue(ps, entity);
    }

    public interface PreparedStatementCreator<T extends Entity<? extends Serializable>> {

        void setValue(PreparedStatement ps, T entity) throws SQLException;
    }
}
