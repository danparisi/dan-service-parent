package com.danservice.techstarter.processor;

import com.danservice.techstarter.processor.SpringConfigImportEnvironmentPostProcessor.SpringProperties.Spring;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.valueOf;

@Slf4j
public class SpringConfigImportEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String PROPERTY_SPRING_CONFIG_IMPORT = "spring.config.import";
    private static final String PROPERTY_SPRING_CLOUD_CONSUL_HOST = "spring.cloud.consul.host";
    private static final String PROPERTY_SPRING_CLOUD_CONSUL_PORT = "spring.cloud.consul.port";

    @Lazy
    public SpringConfigImportEnvironmentPostProcessor() {
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Spring springProperties = getSpringPropertiesValue().getSpring();
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
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private SpringProperties getSpringPropertiesValue() {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(new Constructor(SpringProperties.class, new LoaderOptions()), representer);

        InputStream inputStream =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("application.yml");

        return yaml
                .load(inputStream);
    }

    private static void addProperty(Properties properties, String key, String value) {
        properties.setProperty(key, value);

        log.info("Adding property [{}] with value [{}]",
                key, value);
    }

    @Getter
    @Setter
    static class SpringProperties {
        public Spring spring = new Spring();

        @Getter
        @Setter
        public static class Spring {
            public Map<String, String> config = new HashMap<>();
            public Cloud cloud = new Cloud();
        }

        @Getter
        @Setter
        public static class Cloud {
            public Consul consul = new Consul();
        }

        @Getter
        @Setter
        public static class Consul {
            public int port;
            public String host;
        }
    }
}