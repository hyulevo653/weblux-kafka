package com.duchuy.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
@ComponentScan({"com.duchuy.profileservice", "com.duchuy.commonservice"})
public class ProfileserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileserviceApplication.class, args);
	}

}
