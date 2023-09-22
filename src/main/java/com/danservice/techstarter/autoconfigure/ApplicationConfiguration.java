package com.danservice.techstarter.autoconfigure;

import com.danservice.techstarter.DanServiceProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(DanServiceProperties.class)
public class ApplicationConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(Environment environment) {
        String serviceName = environment.getProperty("spring.application.name");

        /* Exposing application name to Prometheus, so that it's shown on Grafana */
        return registry -> registry.config().commonTags("application", serviceName);
    }

}
