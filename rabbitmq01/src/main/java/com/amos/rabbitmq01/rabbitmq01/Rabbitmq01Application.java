package com.amos.rabbitmq01.rabbitmq01;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan(basePackages = "com.amos.rabbitmq01.rabbitmq01.mapper")
@ImportResource(locations = {"classpath:spring/spring-jdbc.xml"})
@EnableCaching
public class Rabbitmq01Application  extends SpringBootServletInitializer {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper;
    }

    //不使用spring boot内嵌tomcat启动方式
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Rabbitmq01Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Rabbitmq01Application.class, args);
    }

}
