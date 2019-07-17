package com.db.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableEurekaClient
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = { "com.db.product.es" })
public class AppProduct {

    public static void main(String[] args) {
        SpringApplication.run(AppProduct.class, args);
    }

}