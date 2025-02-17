package io.github.djordjije11.reeled.shared.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "sh_stored_event")
public class StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private Long aggregateId;

    private byte[] key;

    private byte[] payload;

    @Enumerated(EnumType.STRING)
    private StoredEventStatus status;

    public StoredEvent(String type, Long aggregateId, byte[] key, byte[] payload) {
        Assert.hasText(type, "type must be provided");
        Assert.notNull(aggregateId, "aggregateId must not be null");
        Assert.notNull(key, "key must not be null");

        this.type = type;
        this.aggregateId = aggregateId;
        this.key = key;
        this.payload = payload;
        this.status = StoredEventStatus.PENDING;
    }

    public void markAsPublished() {
        this.status = StoredEventStatus.PUBLISHED;
    }
}
