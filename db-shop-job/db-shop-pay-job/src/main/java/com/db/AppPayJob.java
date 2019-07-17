package com.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 支付服务任务调度
 */
@SpringBootApplication
public class AppPayJob {
    public static void main(String[] args) {
        SpringApplication.run(AppPayJob.class,args);
    }
}
