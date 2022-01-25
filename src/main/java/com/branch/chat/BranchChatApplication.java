package com.branch.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.branch")
public class BranchChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranchChatApplication.class, args);
	}

}
