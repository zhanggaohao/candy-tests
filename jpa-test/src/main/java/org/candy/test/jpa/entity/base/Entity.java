package org.candy.test.jpa.entity.base;

import java.io.Serializable;

/**
 * BaseEntity
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/11
 */
public interface Entity<ID> extends Serializable {

    ID getId();
}
