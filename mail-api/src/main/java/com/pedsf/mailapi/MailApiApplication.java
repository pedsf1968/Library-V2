package com.pedsf.mailapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
public class MailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailApiApplication.class, args);
	}

}
