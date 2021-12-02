package com.interactions.sandbox.demo.springbootkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication
public class SpringBootKafkaDemoApplication {

	public static void main(String[] args) { 
		SpringApplication.run(SpringBootKafkaDemoApplication.class, args);
	}

}
