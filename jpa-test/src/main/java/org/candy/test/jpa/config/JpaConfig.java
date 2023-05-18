package org.candy.test.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JpaConfig
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/5/10
 */
@ComponentScan(basePackages = "org.candy.test.jpa.dao")
@EnableJpaRepositories(basePackages = "org.candy.test.jpa.dao")
@EntityScan(basePackages = "org.candy.test.jpa.entity")
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
public class JpaConfig {
}
