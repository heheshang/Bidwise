package com.ssk.bidwise;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
@MapperScan(basePackages = "com.ssk.bidwise.dal.postgres", annotationClass = Mapper.class)
public class BidwiseApplication {

    public static void main(String[] args) {

        SpringApplication.run(BidwiseApplication.class, args);
    }

}
