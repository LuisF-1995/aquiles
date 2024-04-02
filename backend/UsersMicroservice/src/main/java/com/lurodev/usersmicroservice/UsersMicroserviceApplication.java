package com.lurodev.usersmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsersMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsersMicroserviceApplication.class, args);
    }

}