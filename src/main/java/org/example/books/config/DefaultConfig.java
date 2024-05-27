package org.example.books.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
//@PropertySource("file:application.yaml")
public class DefaultConfig {
}
