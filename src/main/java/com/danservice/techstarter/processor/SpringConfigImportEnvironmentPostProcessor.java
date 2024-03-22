package com.danservice.techstarter.processor;

import com.danservice.techstarter.SpringProperties;
import com.danservice.techstarter.SpringProperties.Spring;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.InputStream;
import java.util.Properties;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class SpringConfigImportEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String PROPERTY_SPRING_CONFIG_IMPORT = "spring.config.import";
    private static final String PROPERTY_SPRING_CLOUD_CONSUL_HOST = "spring.cloud.consul.host";
    private static final String PROPERTY_SPRING_CLOUD_CONSUL_PORT = "spring.cloud.consul.port";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Spring springProperties = getSpringPropertiesValue().getSpring();
            System.out.printf("Building [SpringConfigImportEnvironmentPostProcessor] from following properties: [%s]%n", springProperties);

            int consulPort = springProperties.getCloud().getConsul().getPort();
            String consulHost = springProperties.getCloud().getConsul().getHost();
            String springConfigImport = springProperties.getConfig().get("import");

            final var customProperties = new Properties(1);
            addProperty(customProperties, PROPERTY_SPRING_CLOUD_CONSUL_HOST, consulHost);
            addProperty(customProperties, PROPERTY_SPRING_CLOUD_CONSUL_PORT, valueOf(consulPort));
            addProperty(customProperties, PROPERTY_SPRING_CONFIG_IMPORT, springConfigImport);

            var propertiesPropertySource = new PropertiesPropertySource("custom-resource", customProperties);
            environment
                    .getPropertySources()
                    .addLast(propertiesPropertySource);
        } catch (Exception ex) {
            /* Without wrapping the exception,
               the service would exit at startup without logging any error */
            ex.printStackTrace(System.out);

            throw ex;
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @SneakyThrows
    private SpringProperties getSpringPropertiesValue() {
        try (InputStream applicationYmlStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream("application.yml")) {

            String applicationYml = new String(
                    requireNonNull(applicationYmlStream).readAllBytes(), UTF_8);

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.findAndRegisterModules();
            objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper.readValue(applicationYml, SpringProperties.class);
        }
    }

    private static void addProperty(Properties properties, String key, String value) {
        properties.setProperty(key, value);
    }
}