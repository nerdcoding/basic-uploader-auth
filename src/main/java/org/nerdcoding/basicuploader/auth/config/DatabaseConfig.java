/*
 * DatabaseConfig.java
 *
 * Copyright (c) 2019, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.basicuploader.auth.config;

import static org.nerdcoding.basicuploader.auth.config.DatabaseConfig.BASE_PACKAGE_TO_SCAN;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

/**
 * Explicit configuration of the data source.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = BASE_PACKAGE_TO_SCAN,
        entityManagerFactoryRef = "userdataEntityManager",
        transactionManagerRef = "userdataTransactionManager"
)
public class DatabaseConfig {

    /** All JPA resources in this base package are considered. */
    static final String BASE_PACKAGE_TO_SCAN = "org.nerdcoding.basicuploader.auth";

    private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.H2Dialect";

    private final String databaseUrl;
    private final String jdbcDriver;
    private final String databaseUser;
    private final String databasePassword;
    private final String hbm2ddl;

    @Autowired
    public DatabaseConfig(
            @Value("${auth.database.url}") final String databaseUrl,
            @Value("${auth.database.user}") final String databaseUser,
            @Value("${auth.database.password}") final String databasePassword,
            @Value("${auth.database.jdbc.driver}") final String jdbcDriver,
            @Value("${auth.database.hbm2ddl:none}") final String hbm2ddl) {
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.jdbcDriver = jdbcDriver;
        this.hbm2ddl = hbm2ddl;
    }

    @Bean("userdataEntityManager")
    public LocalContainerEntityManagerFactoryBean userdataEntityManager(
            @Qualifier("userdataDataSource") final ComboPooledDataSource userdataDataSource) {
        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(userdataDataSource);
        entityManager.setPackagesToScan(BASE_PACKAGE_TO_SCAN);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaPropertyMap(createJpaProperties());

        return entityManager;
    }

    @Bean("userdataDataSource")
    public ComboPooledDataSource userdataDataSource() throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(databaseUrl);
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setUser(databaseUser);
        dataSource.setPassword(databasePassword);

        return dataSource;
    }

    @Bean("userdataTransactionManager")
    public PlatformTransactionManager userdataTransactionManager(
            @Qualifier("userdataEntityManager") final LocalContainerEntityManagerFactoryBean userdataEntityManager) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userdataEntityManager.getObject());

        return transactionManager;
    }

    private Map<String, Object> createJpaProperties() {
        final Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddl);
        jpaProperties.put("hibernate.dialect", HIBERNATE_DIALECT);

        return jpaProperties;
    }
}
