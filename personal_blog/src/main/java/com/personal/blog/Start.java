package com.personal.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@MapperScan("com.personal.blog.mapper")
@PropertySource({"classpath:config/jwt.properties", "classpath:config/filterpath.properties", "classpath:config/db.properties"})
public class Start {
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }
}
