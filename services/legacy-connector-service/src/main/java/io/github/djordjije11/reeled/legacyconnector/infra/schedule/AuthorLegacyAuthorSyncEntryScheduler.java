package io.github.djordjije11.reeled.legacyconnector.infra.schedule;

import io.github.djordjije11.reeled.legacyconnector.application.AuthorLegacyAuthorSyncEntryService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorLegacyAuthorSyncEntryScheduler {

    private final AuthorLegacyAuthorSyncEntryService authorLegacyAuthorSyncEntryService;

    @Scheduled(cron = "${reeled.legacyconnector.infra.schedule.author-legacy-author-sync-entry-reconciliation.cron}")
    @SchedulerLock(name = "triggerAuthorLegacyAuthorSyncEntryReconciliation")
    public void triggerAuthorLegacyAuthorSyncEntryReconciliation() {
        authorLegacyAuthorSyncEntryService.reconcile();
    }
}
