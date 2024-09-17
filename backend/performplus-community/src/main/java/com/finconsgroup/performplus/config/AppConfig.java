package com.finconsgroup.performplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

@Configuration
public class AppConfig {

    @Bean
     ResponseStatusExceptionResolver responseStatusExceptionResolver(){
        ResponseStatusExceptionResolver resolver = new ResponseStatusExceptionResolver();
        resolver.setWarnLogCategory("org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver.warnLogger");
        return resolver;
    }
}