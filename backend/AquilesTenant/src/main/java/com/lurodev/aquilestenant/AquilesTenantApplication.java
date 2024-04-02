package com.lurodev.aquilestenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AquilesTenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(AquilesTenantApplication.class, args);
    }

}
