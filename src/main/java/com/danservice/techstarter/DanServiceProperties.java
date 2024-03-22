package com.danservice.techstarter;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "dan")
public class DanServiceProperties {

    private DanServiceKafkaProperties kafka = new DanServiceKafkaProperties();

    @Getter
    @Setter
    public static class DanServiceKafkaProperties {

        private boolean enabled = true;

        private DanServiceKafkaProducerProperties producer = new DanServiceKafkaProducerProperties();
        private DanServiceKafkaConsumerProperties consumer = new DanServiceKafkaConsumerProperties();
    }

    @Getter
    @Setter
    public static class DanServiceKafkaProducerProperties {

        private boolean enabled = true;

        private boolean logback = true;

        private DanServiceKafkaProducerTopicsProperties topics = new DanServiceKafkaProducerTopicsProperties();

    }

    @Getter
    @Setter
    public static class DanServiceKafkaConsumerProperties {

        private boolean enabled = true;

    }

    @Getter
    @Setter
    public static class DanServiceKafkaProducerTopicsProperties {

        @NotEmpty
        private String logs;

    }
}