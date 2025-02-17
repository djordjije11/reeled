package io.github.djordjije11.reeled.shared.infra.schedule;

import io.github.djordjije11.reeled.shared.application.StoredEventService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@ConditionalOnProperty(prefix = "reeled.shared.infra.schedule.stored-event-scheduler", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Component
public class StoredEventScheduler {

    private final StoredEventService storedEventService;

    @Scheduled(fixedDelay = 200)
    @SchedulerLock(name = "triggerStoredEventPublishing")
    public void triggerStoredEventPublishing() {
        storedEventService.publishPending();
    }
}
