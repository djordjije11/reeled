package io.github.djordjije11.reeled.post.infra.schedule;

import io.github.djordjije11.reeled.post.application.PostService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostScheduler {

    private final PostService postService;

    @Scheduled(cron = "${reeled.post.infra.schedule.post-purge.cron}")
    @SchedulerLock(name = "triggerPostPurge")
    public void triggerPostPurge() {
        postService.purge();
    }
}
