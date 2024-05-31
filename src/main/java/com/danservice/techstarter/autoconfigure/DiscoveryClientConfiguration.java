package com.danservice.techstarter.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("kubernetes")
public class DiscoveryClientConfiguration {

    @Bean
    @Lazy
    ReactiveDiscoveryClient myKubernetesReactiveDiscoveryClient(@Value("${NAMESPACE}") String currentNamespace) {
        return new MyKubernetesReactiveDiscoveryClient(currentNamespace);
    }

    @Bean
    @Lazy
    DiscoveryClient myKubernetesDiscoveryClient(@Value("${NAMESPACE}") String currentNamespace) {
        return new MyKubernetesDiscoveryClient(currentNamespace);
    }
}
