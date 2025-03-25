package com.learn.mydy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.learn.mydy.mapper")
public class MyDyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDyApplication.class, args);
    }

}
