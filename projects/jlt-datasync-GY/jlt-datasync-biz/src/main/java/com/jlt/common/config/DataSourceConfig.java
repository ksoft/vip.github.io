package com.jlt.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "saasDataSource")
    @Qualifier("saasDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource saasDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "upmDataSource")
    @Qualifier("upmDataSource")
    @ConfigurationProperties(prefix = "spring.upm.datasource")
    public DataSource upmDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pmsDataSource")
    @Qualifier("pmsDataSource")
    @ConfigurationProperties(prefix = "spring.pms.datasource")
    public DataSource pmsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "saasJdbcTemplate")
    public JdbcTemplate saasJdbcTemplate(@Qualifier("saasDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "upmJdbcTemplate")
    public JdbcTemplate upmJdbcTemplate(@Qualifier("upmDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "pmsJdbcTemplate")
    public JdbcTemplate pmsJdbcTemplate(@Qualifier("pmsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
