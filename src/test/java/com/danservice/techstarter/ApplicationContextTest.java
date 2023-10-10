package com.danservice.techstarter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.danservice.techstarter.ApplicationContextTest.ApplicationTestConfiguration;

@EmbeddedKafka
@SpringBootTest
@Import(ApplicationTestConfiguration.class)
class ApplicationContextTest {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	@SpringBootApplication
	public static class ApplicationTestConfiguration {
	}
}
