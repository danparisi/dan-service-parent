package com.danservice.techstarter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.danservice.techstarter.DemoApplicationTests.ApplicationTestConfiguration;

@EmbeddedKafka
@SpringBootTest
@Import(ApplicationTestConfiguration.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	@SpringBootApplication
	public static class ApplicationTestConfiguration {
	}
}
