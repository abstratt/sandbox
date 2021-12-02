package com.interactions.sandbox.demo.springbootkafka;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleKafkaListener {
	private Logger logger = LoggerFactory.getLogger(SimpleKafkaListener.class);
	
	public SimpleKafkaListener() {
		logger.info("Listener started");
	}
	
	@KafkaListener(topics = "time-request", idIsGroup = true, id = "simple-kafka-listener")
	@SendTo("time-response")
	public Message<TimeResponse> consumeGreeting(String message) throws UnknownHostException {
		logger.info("Message received: " + message);
		TimeResponse payload = new TimeResponse(ZonedDateTime.now(), message, InetAddress.getLocalHost().getHostName());
		return MessageBuilder.withPayload(payload).setReplyChannelName("time-response").build();
	}
}
