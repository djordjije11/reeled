package io.github.djordjije11.reeled.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
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

    @Bean
    MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
