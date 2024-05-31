package com.danservice.techstarter.autoconfigure;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class MyKubernetesReactiveDiscoveryClient extends MyBaseKubernetesDiscoveryClient implements ReactiveDiscoveryClient {

    public MyKubernetesReactiveDiscoveryClient(String currentNamespace) {
        super(currentNamespace);
    }

    @Override
    public String description() {
        return "Custom reactive kubernetes discovery client";
    }

    @Override
    @SneakyThrows
    public Flux<ServiceInstance> getInstances(String serviceId) {
        List<ServiceInstance> serviceInstances = retrieveInstances(serviceId);

        log.info("Discovered service [{}] instances: [{}]", serviceId, serviceInstances);
        return Flux.fromIterable(serviceInstances);
    }

    @Override
    @SneakyThrows
    public Flux<String> getServices() {
        List<String> serviceIds = retrieveServices();

        log.info("Discovered service IDs: [{}]", serviceIds);
        return Flux.fromIterable(serviceIds);
    }
}
