package com.example.library.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.example.library")
@EnableAspectJAutoProxy
@Import(DbConfig.class)
public class AppConfig {
}
