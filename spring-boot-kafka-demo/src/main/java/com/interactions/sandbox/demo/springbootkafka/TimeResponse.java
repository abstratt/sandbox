package com.interactions.sandbox.demo.springbootkafka;

import java.time.ZonedDateTime;

public record TimeResponse(ZonedDateTime current, String request, String provider) {
}
