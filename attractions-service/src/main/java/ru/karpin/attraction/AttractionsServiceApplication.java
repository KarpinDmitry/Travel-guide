package ru.karpin.attraction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttractionsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttractionsServiceApplication.class, args);
	}
}
