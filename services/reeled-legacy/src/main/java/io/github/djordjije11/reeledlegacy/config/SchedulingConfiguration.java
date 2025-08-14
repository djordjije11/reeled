package io.github.djordjije11.reeledlegacy.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * @author Djordjije Radovic
 */
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT1H")
class SchedulingConfiguration {

    @Bean
    LockProvider lockProvider(DataSource dataSource) {
        return new JdbcLockProvider(dataSource);
    }
}
