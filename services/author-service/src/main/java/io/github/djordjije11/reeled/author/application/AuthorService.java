package io.github.djordjije11.reeled.author.application;

import io.github.djordjije11.reeled.author.domain.Author;
import io.github.djordjije11.reeled.author.domain.AuthorFactory;
import io.github.djordjije11.reeled.author.domain.AuthorPurged;
import io.github.djordjije11.reeled.author.domain.AuthorRepository;
import io.github.djordjije11.reeled.author.domain.AuthorSupportRepository;
import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.ClockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private static final Period RETENTION_PERIOD = Period.ofDays(3);

    private static final int FETCH_SIZE = 5000;

    private final AuthorFactory authorFactory;

    private final AuthorRepository authorRepository;

    private final AuthorSupportRepository authorSupportRepository;

    private final AuthorPurgedEventPublisher authorPurgedEventPublisher;

    private final io.github.resilience4j.retry.Retry dataAccessRetry;

    private final Clock clock;

    public AuthorService(AuthorFactory authorFactory,
                         AuthorRepository authorRepository,
                         AuthorSupportRepository authorSupportRepository,
                         AuthorPurgedEventPublisher authorPurgedEventPublisher,
                         RetryRegistry retryRegistry,
                         ClockProvider clockProvider) {
        this.authorFactory = authorFactory;
        this.authorRepository = authorRepository;
        this.authorSupportRepository = authorSupportRepository;
        this.authorPurgedEventPublisher = authorPurgedEventPublisher;
        this.dataAccessRetry = retryRegistry.retry(DATA_ACCESS_RETRY_NAME);
        this.clock = clockProvider.getClock();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public Long create(Long id, String name, AuthorType type, String bio, String imageUrl, boolean legacy) {
        logger.info("Creating an author (name: {})...", name);

        final Author author = authorFactory.create(id, name, type, bio, imageUrl, legacy);

        authorRepository.save(author);

        logger.info("Author successfully created (id: {}, name: {})", author.getId(), name);

        return author.getId();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void update(Long id, String name, String bio, String imageUrl, boolean legacy) {
        Assert.notNull(id, "id must not be null");

        logger.info("Updating an author (id: {})...", id);

        final Author author = getAuthor(id);
        author.update(name, bio, imageUrl, legacy);

        authorRepository.save(author);

        logger.info("Author successfully updated (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting an author (id: {})...", id);

        final Author author = getAuthor(id);
        author.delete(clock);

        authorRepository.save(author);

        logger.info("Author successfully deleted (id: {})", id);
    }

    public void purge() {
        logger.info("Purging authors...");

        final ZonedDateTime purgeAgeThreshold = ZonedDateTime.now(clock).minus(RETENTION_PERIOD);

        List<Long> ids;
        do {
            ids = authorSupportRepository.findAllIdsEligibleForPurge(purgeAgeThreshold, FETCH_SIZE);
            ids.forEach(id -> {
                try {
                    dataAccessRetry.executeRunnable(() -> purge(id));
                } catch (RuntimeException e) {
                    logger.error("Error occurred while purging an author (id: {})", id, e);
                }
            });
        } while (ids.size() == FETCH_SIZE);

        logger.info("Authors successfully purged");
    }

    private void purge(Long id) {
        logger.info("Purging an author (id: {})...", id);

        final Author author = authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));

        authorPurgedEventPublisher.publishAuthorPurged(new AuthorPurged(id));

        authorRepository.delete(author);

        logger.info("Author successfully purged (id: {})", id);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
