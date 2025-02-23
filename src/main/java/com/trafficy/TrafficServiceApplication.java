package com.trafficy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrafficServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrafficServiceApplication.class, args);
	}

}
