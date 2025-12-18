package com.library.API_GATEway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // May be optional, but good for clarity

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatEwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatEwayApplication.class, args);
    }
}