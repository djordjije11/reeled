package io.github.djordjije11.reeled.author.infra.schedule;

import io.github.djordjije11.reeled.author.application.AuthorService;
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

    @Scheduled(cron = "${reeled.author.infra.schedule.author-purge.cron}")
    @SchedulerLock(name = "triggerAuthorPurge")
    public void triggerAuthorPurge() {
        authorService.purge();
    }
}
