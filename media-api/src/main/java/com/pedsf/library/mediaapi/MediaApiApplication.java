package com.pedsf.library.mediaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MediaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaApiApplication.class, args);
	}

}
