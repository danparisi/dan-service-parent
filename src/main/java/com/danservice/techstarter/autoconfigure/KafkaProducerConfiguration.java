package com.danservice.techstarter.autoconfigure;

import io.micrometer.common.KeyValues;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.micrometer.KafkaRecordSenderContext;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservationConvention;

@Configuration
@ConditionalOnProperty(prefix = "dan", name = "kafka.producer.enabled", havingValue = "true")
public class KafkaProducerConfiguration {
    private static final String TAG_KEY = "key";
    private static final String TAG_TOPIC = "topic";

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(final ProducerFactory<?, ?> producerFactory) {
        final KafkaTemplate<?, ?> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate.setObservationConvention(new KafkaTemplateObservationConvention() {
            @NotNull
            @Override
            public KeyValues getLowCardinalityKeyValues(@NotNull KafkaRecordSenderContext context) {
                return KeyValues.of(
                        TAG_TOPIC, context.getDestination(),
                        TAG_KEY, String.valueOf(context.getRecord().key()));
            }
        });

        return kafkaTemplate;
    }

}
