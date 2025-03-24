package com.example.springserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.springserver", "com.example.springserver.config"})
public class SpringserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringserverApplication.class, args);
	}

}
