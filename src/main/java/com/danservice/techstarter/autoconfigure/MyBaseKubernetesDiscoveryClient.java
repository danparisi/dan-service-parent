package com.danservice.techstarter.autoconfigure;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Slf4j
public class MyBaseKubernetesDiscoveryClient {
    private static final String LABEL_APP_KUBERNETES_IO_INSTANCE = "app.kubernetes.io/instance";

    private final String currentNamespace;
    private final CoreV1Api kubernetesApi;

    @SneakyThrows
    public MyBaseKubernetesDiscoveryClient(String currentNamespace) {
        ApiClient client = Config.defaultClient();
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);

        this.kubernetesApi = new CoreV1Api();
        this.currentNamespace = currentNamespace;

        log.info("Discovery client constructed. Instances discovered: [{}]", retrieveServices());
    }

    @SneakyThrows
    public List<ServiceInstance> retrieveInstances(final String serviceId) {
        try {
            String labelSelector = LABEL_APP_KUBERNETES_IO_INSTANCE + "=" + serviceId;
            //V1PodList podList = kubernetesApi.listNamespacedPod(currentNamespace).labelSelector(labelSelector).execute();
            V1PodList podList = kubernetesApi.listNamespacedPod(currentNamespace, null, null, null, null, labelSelector, null, null, null, null, 5, false);

            return podList.getItems().stream()
                    .filter(v1Pod -> nonNull(v1Pod.getMetadata()) && nonNull(v1Pod.getStatus()))
                    .map(v1Pod -> mapToDefaultServiceInstance(serviceId, v1Pod))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());
        } catch (ApiException apiException) {
            log.error("Error while discovering service instances: " + apiException.getResponseBody(), apiException);

            throw apiException;
        }
    }

    @SneakyThrows
    public List<String> retrieveServices() {
        try {
            //V1PodList instances = kubernetesApi.listNamespacedPod(currentNamespace).execute();
            V1PodList instances = kubernetesApi.listNamespacedPod(currentNamespace, null, null, null, null, null, null, null, null, null, 5, false);

            return instances
                    .getItems().stream()
                    .filter(v1Pod -> nonNull(v1Pod.getMetadata()) && nonNull(v1Pod.getMetadata().getLabels()))
                    .map(v1Pod -> v1Pod.getMetadata().getLabels().get(LABEL_APP_KUBERNETES_IO_INSTANCE))
                    .collect(toList());
        } catch (ApiException apiException) {
            log.error("Error while discovering services: " + apiException.getResponseBody(), apiException);

            throw apiException;
        }
    }

    @NotNull
    private Optional<DefaultServiceInstance> mapToDefaultServiceInstance(String serviceId, V1Pod v1Pod) {
        if (isNull(v1Pod.getMetadata())) {
            log.warn("Skipping kubernetes Pod because empty metadata. V1Pod=[{}]", v1Pod);

            return empty();
        }

        if (isNull(v1Pod.getStatus())) {
            log.warn("Skipping kubernetes Pod because empty status. V1Pod=[{}]", v1Pod);

            return empty();
        }

/*
  Extracting container port from V1Pod is too verbose, let's find another way (i.e. state the container port in some label)
 */
//        Optional<Integer> containerPort = v1Pod.getSpec().getContainers().stream()
//                .filter(v1Container -> v1Container.getName().equals(serviceId))
//                .findFirst()
//                .map(V1Container::getPorts)
//                .map(List::getFirst)
//                .map(V1ContainerPort::getContainerPort);

        return of(new DefaultServiceInstance(v1Pod.getMetadata().getName(), serviceId, v1Pod.getStatus().getPodIP(), 8080, false));
    }

}
