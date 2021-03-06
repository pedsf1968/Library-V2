package com.pedsf.library.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.pedsf.library.webapi")
public class WebApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebApplication.class, args);

	}
}
