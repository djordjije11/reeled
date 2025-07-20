package io.github.djordjije11.reeled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
public class LegacyConnectorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyConnectorServiceApplication.class, args);
    }
}
