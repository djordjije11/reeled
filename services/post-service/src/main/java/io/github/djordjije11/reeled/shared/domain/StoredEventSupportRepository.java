package io.github.djordjije11.reeled.shared.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @author Djordjije Radovic
 */
public interface StoredEventSupportRepository extends Repository<StoredEvent, Long> {

    @Query("""
            SELECT new io.github.djordjije11.reeled.shared.domain.StoredEventProjection(id, type, aggregateId, key, payload)
            FROM StoredEvent
            WHERE status = :status
            ORDER BY id
            LIMIT :limit""")
    List<StoredEventProjection> findAllByStatus(StoredEventStatus status, int limit);
}
