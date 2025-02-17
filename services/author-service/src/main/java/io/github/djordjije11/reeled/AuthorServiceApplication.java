package io.github.djordjije11.reeled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
public class AuthorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorServiceApplication.class, args);
    }
}
