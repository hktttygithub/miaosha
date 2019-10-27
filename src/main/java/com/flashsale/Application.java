package com.flashsale;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.flashsale.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("hello");
    }
}
