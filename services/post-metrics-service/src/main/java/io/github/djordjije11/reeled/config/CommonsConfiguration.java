package io.github.djordjije11.reeled.config;

import jakarta.validation.ClockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * @author Djordjije Radovic
 */
@Configuration
class CommonsConfiguration {

    @Bean
    ClockProvider clockProvider() {
        return Clock::systemUTC;
    }
}
