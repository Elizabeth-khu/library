package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class DbConfig {

    @Bean
    public DataSource dataSource(Environment env) {
        var ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(required(env));
        ds.setUsername(env.getProperty("db.username", ""));
        ds.setPassword(env.getProperty("db.password", ""));
        return ds;

    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private String required(Environment env) {
        String v = env.getProperty("db.url");
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing property: " + "db.url");
        }
        return v;
    }

}
