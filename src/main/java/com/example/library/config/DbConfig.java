package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class DbConfig {

    @Bean
    public DataSource dataSource(Environment env) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(requiredProperty(env, "db.url"));
        ds.setUsername(env.getProperty("db.username", ""));
        ds.setPassword(env.getProperty("db.password", ""));
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private String requiredProperty(Environment env, String key) {
        String value = env.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing property: " + key);
        }
        return value;
    }
}