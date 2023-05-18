package org.candy.test.jpa.entity.base;

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serial;
import java.io.Serializable;

/**
 * AbstractEntity
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/11
 */
public abstract class AbstractEntity<ID extends Serializable> extends AbstractPersistable<ID>
        implements Entity<ID> {

    @Serial
    private static final long serialVersionUID = -2063284299575632081L;

    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
