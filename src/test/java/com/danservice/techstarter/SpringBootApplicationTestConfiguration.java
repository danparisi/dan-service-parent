package com.danservice.techstarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.net.URL;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@Slf4j
@TestConfiguration
@EnableFeignClients
@SpringBootApplication
public class SpringBootApplicationTestConfiguration {

    @Test
    void testApplicationYmlResourceLoad() {
        var yaml = getSpringPropertiesValue();

        SpringProperties.Spring springProperties = yaml.getSpring();
        log.info("Consul host: [{}]", springProperties.getCloud().getConsul().getHost());
        log.info("Consul port: [{}]", springProperties.getCloud().getConsul().getPort());
        log.info("Config import: [{}]", springProperties.getConfig().get("import"));
    }

    @Test
    void testApplicationYmlResourceLoad2() {
        var yaml = getSpringPropertiesValue2();

        SpringProperties.Spring springProperties = yaml.getSpring();
        log.info("Consul host: [{}]", springProperties.getCloud().getConsul().getHost());
        log.info("Consul port: [{}]", springProperties.getCloud().getConsul().getPort());
        log.info("Config import: [{}]", springProperties.getConfig().get("import"));
    }

    @SneakyThrows
    private SpringProperties getSpringPropertiesValue() {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(new Constructor(SpringProperties.class, new LoaderOptions()), representer);

        try (InputStream applicationYmlStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream("application.yml")) {

            String applicationYml = new String(
                    requireNonNull(applicationYmlStream).readAllBytes(), UTF_8);

            System.out.printf("Loaded [application.yml] resource: [%s]%n", applicationYml);

            return yaml
                    .load(applicationYml);
        }
    }

    @SneakyThrows
    private SpringProperties getSpringPropertiesValue2() {
        URL url = getClass()
                .getClassLoader()
                .getResource("application.yml");

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(url, SpringProperties.class);
    }
}