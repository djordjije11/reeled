package io.github.djordjije11.reeled.config;

import io.github.djordjije11.reeled.post.domain.PostPurged;
import io.github.djordjije11.reeled.post.event.PostDeleted;
import io.github.djordjije11.reeled.post.event.PostDeletedKey;
import io.github.djordjije11.reeled.post.event.PostUpserted;
import io.github.djordjije11.reeled.post.event.PostUpsertedKey;
import io.github.djordjije11.reeled.shared.application.StoredEventPublisher;
import io.github.djordjije11.reeled.shared.domain.EventStoreProperties;
import io.github.djordjije11.reeled.shared.infra.messaging.KafkaStoredEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author Djordjije Radovic
 */
@Configuration
class EventStoreConfiguration {

    private static final String POST_EVENT_TOPIC_BINDING = "postPostEventSource-out-0";

    @Bean
    public EventStoreProperties eventStoreProperties() {
        return new EventStoreProperties(Map.of(PostUpserted.class,
                List.of(new EventStoreProperties.DomainEventConfiguration<>("PostUpserted",
                        POST_EVENT_TOPIC_BINDING,
                        PostUpserted::getId,
                        id -> PostUpsertedKey.newBuilder().setId(id).build())),
                PostDeleted.class,
                List.of(new EventStoreProperties.DomainEventConfiguration<>("PostDeleted",
                        POST_EVENT_TOPIC_BINDING,
                        PostDeleted::getId,
                        id -> PostDeletedKey.newBuilder().setId(id).build())),
                PostPurged.class,
                List.of(new EventStoreProperties.DomainEventConfiguration<>("PostUpsertedTombstone",
                                POST_EVENT_TOPIC_BINDING,
                                PostPurged::getId,
                                id -> PostUpsertedKey.newBuilder().setId(id).build()),
                        new EventStoreProperties.DomainEventConfiguration<>("PostDeletedTombstone",
                                POST_EVENT_TOPIC_BINDING,
                                PostPurged::getId,
                                id -> PostDeletedKey.newBuilder().setId(id).build()))));
    }

    @Bean
    public StoredEventPublisher storedEventPublisher(StreamBridge streamBridge) {
        return new KafkaStoredEventPublisher(streamBridge);
    }
}
