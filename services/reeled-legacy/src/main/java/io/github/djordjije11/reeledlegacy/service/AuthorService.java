package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.model.Author;
import io.github.djordjije11.reeledlegacy.model.AuthorType;
import io.github.djordjije11.reeledlegacy.repository.AuthorRepository;
import io.github.djordjije11.reeledlegacy.repository.AuthorTypeRepository;
import jakarta.validation.ClockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private static final Period RETENTION_PERIOD = Period.ofDays(3);

    private static final int FETCH_SIZE = 5000;

    private final AuthorRepository authorRepository;

    private final AuthorTypeRepository authorTypeRepository;

    private final Clock clock;

    public AuthorService(AuthorRepository authorRepository, AuthorTypeRepository authorTypeRepository, ClockProvider clockProvider) {
        this.authorRepository = authorRepository;
        this.authorTypeRepository = authorTypeRepository;
        this.clock = clockProvider.getClock();
    }

    public Long create(String name, Long typeId, String bio, String imageUrl) {
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(typeId, "typeId must not be null");

        logger.info("Creating an author (name: {})...", name);

        final AuthorType authorType = authorTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Author type does not exist (id: %d)".formatted(typeId)));

        final Author author = new Author();
        author.setName(name);
        author.setType(authorType);
        author.setBio(bio);
        author.setImageUrl(imageUrl);

        authorRepository.save(author);

        logger.info("Author successfully created (id: {}, name: {})", author.getId(), name);

        return author.getId();
    }

    public void update(Long id, String name, String bio, String imageUrl) {
        Assert.notNull(id, "id must not be null");
        Assert.hasText(name, "name must not be empty");

        logger.info("Updating an author (id: {})...", id);

        final Author author = getAuthor(id);
        author.setName(name);
        author.setBio(bio);
        author.setImageUrl(imageUrl);

        authorRepository.save(author);

        logger.info("Author successfully updated (id: {})", id);
    }

    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting an author (id: {})...", id);

        final Author author = getAuthor(id);
        author.setDeleted(true);
        author.setDeletedDate(ZonedDateTime.now(clock));

        authorRepository.save(author);

        logger.info("Author successfully deleted (id: {})", id);
    }

    public void purge() {
        logger.info("Purging authors...");

        final ZonedDateTime purgeAgeThreshold = ZonedDateTime.now(clock).minus(RETENTION_PERIOD);

        List<Long> ids;
        do {
            ids = authorRepository.findAllIdsEligibleForPurge(purgeAgeThreshold, FETCH_SIZE);
            ids.forEach(this::purge);
        } while (ids.size() == FETCH_SIZE);

        logger.info("Authors successfully purged");
    }

    private void purge(Long id) {
        logger.info("Purging an author (id: {})...", id);

        final Author author = authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author does not exist (id: %d)".formatted(id)));

        authorRepository.delete(author);

        logger.info("Author successfully purged (id: {})", id);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new RuntimeException("Author does not exist (id: %d)".formatted(id)));
    }
}
