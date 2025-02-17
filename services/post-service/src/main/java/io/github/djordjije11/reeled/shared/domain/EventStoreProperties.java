package io.github.djordjije11.reeled.shared.domain;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
public class EventStoreProperties {

    private final Map<Class<?>, List<DomainEventConfiguration<?>>> domainEventConfigurations;

    private final Map<String, String> typeBindingMap;

    public EventStoreProperties(Map<Class<?>, List<DomainEventConfiguration<?>>> domainEventConfigurations) {
        this.domainEventConfigurations = domainEventConfigurations;
        this.typeBindingMap = domainEventConfigurations.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(DomainEventConfiguration::name, DomainEventConfiguration::binding, (first, second) -> {
                    throw new IllegalArgumentException("Duplicate DomainEventConfiguration name %s detected".formatted(first));
                }));
    }

    public List<DomainEventConfiguration<?>> findByClass(Class<?> clazz) {
        Assert.notNull(clazz, "clazz must not be null");

        return domainEventConfigurations.getOrDefault(clazz, Collections.emptyList());
    }

    public Optional<String> findBindingByType(String type) {
        Assert.notNull(type, "type must not be null");

        return Optional.ofNullable(typeBindingMap.getOrDefault(type, null));
    }

    public record DomainEventConfiguration<T>(String name, String binding, Function<T, Long> aggregateIdGetter, Function<Long, ?> toKey) {

        public DomainEventConfiguration {
            Assert.hasText(name, "name must be provided");
            Assert.hasText(binding, "binding must be provided");
            Assert.notNull(aggregateIdGetter, "aggregateIdGetter must not be null");
            Assert.notNull(toKey, "toKey must not be null");
        }

        public Long getAggregateId(T event) {
            return aggregateIdGetter.apply(event);
        }

        public Object toKey(Long id) {
            return toKey.apply(id);
        }
    }
}
