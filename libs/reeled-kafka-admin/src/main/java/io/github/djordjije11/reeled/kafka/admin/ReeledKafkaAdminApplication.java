package io.github.djordjije11.reeled.kafka.admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Djordjije Radovic
 */
public final class ReeledKafkaAdminApplication {

    private static final Logger logger = LoggerFactory.getLogger(ReeledKafkaAdminApplication.class);

    private ReeledKafkaAdminApplication() {

    }

    public static void main(String[] args) {
        logger.info("Starting the app...");

        if (args.length < 2) {
            throw new IllegalArgumentException("Server and at least one topic need to be specified");
        }

        final String server = args[0];
        final List<String> topicNames = Arrays.asList(args).subList(1, args.length);

        logger.info("server: {}", server);
        logger.info("topicNames: {}", topicNames);

        try (final AdminClient adminClient = KafkaAdminClient.create(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server))) {
            createTopics(adminClient, topicNames);
        }

        logger.info("Shutting down the app...");
    }

    static void createTopics(AdminClient adminClient, final List<String> topicNames) {
        logger.info("Creating topics: {}...", topicNames);

        final List<NewTopic> newTopics = topicNames.stream().map(t -> new NewTopic(t, 1, (short) 1)).toList();

        final CreateTopicsResult createTopicsResult = adminClient.createTopics(newTopics);

        topicNames.forEach(topicName -> {
            try {
                createTopicsResult.values().get(topicName).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Exception while awaiting the topic creation results", e);
            }
        });

        logger.info("Topics {} created", topicNames);
    }
}
