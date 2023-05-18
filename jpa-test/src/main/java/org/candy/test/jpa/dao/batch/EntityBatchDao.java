package org.candy.test.jpa.dao.batch;

import org.candy.test.jpa.entity.base.Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * UserBatchDao
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
public interface EntityBatchDao {

    <T extends Entity<? extends Serializable>> List<T> batchUpdate(Collection<T> entities, BatchSqlParameterResolver<T> factory);
}
