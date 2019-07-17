package com.db;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付服务
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
@MapperScan("com.db.pay.mapper")
public class AppPay {
    public static void main(String[] args) {
        //new SpringApplicationBuilder(AppPay.class).run(args);
        SpringApplication.run(AppPay.class,args);
    }
}
