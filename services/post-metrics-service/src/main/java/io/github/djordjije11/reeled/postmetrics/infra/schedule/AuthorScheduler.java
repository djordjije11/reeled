package io.github.djordjije11.reeled.postmetrics.infra.schedule;

import io.github.djordjije11.reeled.postmetrics.application.AuthorService;
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

    @Scheduled(cron = "${reeled.postmetrics.infra.schedule.author-analytics-monthly-reports-process.cron}")
    @SchedulerLock(name = "triggerAuthorAnalyticsMonthlyReportsProcess", lockAtMostFor = "PT2H")
    public void triggerAuthorAnalyticsMonthlyReportsProcess() {
        authorService.processAnalyticsMonthlyReports();
    }
}
