package com.danservice.techstarter;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
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
        @NotNull
        private Integer maxCheckRate;
    }

    @Getter
    @Setter
    public static class DanServiceKafkaProducerProperties {

        private DanServiceKafkaProducerProperties producer = new DanServiceKafkaProducerProperties();

    }

    @Getter
    @Setter
    public static class DanServiceKafkaProducerTopicsProperties {

        @NotEmpty
        private String logs;

    }
}