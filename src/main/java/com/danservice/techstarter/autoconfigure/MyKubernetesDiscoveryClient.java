package com.danservice.techstarter.autoconfigure;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@Slf4j
public class MyKubernetesDiscoveryClient extends MyBaseKubernetesDiscoveryClient implements DiscoveryClient {
    public MyKubernetesDiscoveryClient(String currentNamespace) {
        super(currentNamespace);
    }

    @Override
    public String description() {
        return "Custom kubernetes discovery client";
    }

    @Override
    @SneakyThrows
    public List<ServiceInstance> getInstances(String serviceId) {
        List<ServiceInstance> serviceInstances = retrieveInstances(serviceId);

        log.info("Discovered service [{}] instances: [{}]", serviceId, serviceInstances);

        return serviceInstances;
    }

    @Override
    @SneakyThrows
    public List<String> getServices() {
        List<String> serviceIds = retrieveServices();

        log.info("Discovered service IDs: [{}]", serviceIds);
        return serviceIds;
    }
}
