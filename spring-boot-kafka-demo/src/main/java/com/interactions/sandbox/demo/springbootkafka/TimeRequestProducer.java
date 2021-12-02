package com.interactions.sandbox.demo.springbootkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TimeRequestProducer implements ApplicationRunner {

	private Logger logger = LoggerFactory.getLogger(TimeRequestProducer.class);
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Running");
		
	}
	
}
