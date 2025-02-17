package io.github.djordjije11.reeled.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaConnectionDetails;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaBinderConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Djordjije Radovic
 */
@Configuration
class CloudStreamConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.stream.kafka.binder")
    KafkaBinderConfigurationProperties kafkaBinderConfigurationProperties(KafkaProperties kafkaProperties,
                                                                          ObjectProvider<KafkaConnectionDetails> kafkaConnectionDetailsProvider) {
        return new KafkaBinderConfigurationProperties(kafkaProperties, kafkaConnectionDetailsProvider);
    }

    @Bean
    public Serializer<Object> kafkaKeySerializer(
            @Qualifier("kafkaBinderConfigurationProperties") KafkaBinderConfigurationProperties binderConfigurationProperties) {
        final KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(binderConfigurationProperties.mergedProducerConfiguration(), true);
        return serializer;
    }

    @Bean
    public Serializer<Object> kafkaValueSerializer(
            @Qualifier("kafkaBinderConfigurationProperties") KafkaBinderConfigurationProperties binderConfigurationProperties) {
        final KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(binderConfigurationProperties.mergedProducerConfiguration(), false);
        return serializer;
    }
}
