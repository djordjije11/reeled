package io.github.djordjije11.reeledlegacy.scheduler;

import io.github.djordjije11.reeledlegacy.service.AuthorService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class AuthorScheduler {

    private final AuthorService authorService;

    @Scheduled(cron = "${reeledlegacy.scheduler.author-analytics-monthly-reports-process.cron}")
    @SchedulerLock(name = "triggerAuthorAnalyticsMonthlyReportsProcess", lockAtMostFor = "PT2H")
    public void triggerAuthorAnalyticsMonthlyReportsProcess() {
        authorService.processAnalyticsMonthlyReports();
    }
}
