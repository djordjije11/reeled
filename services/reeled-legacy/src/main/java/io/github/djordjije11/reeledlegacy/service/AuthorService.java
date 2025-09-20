package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.commons.exception.ReeledException;
import io.github.djordjije11.reeledlegacy.model.Author;
import io.github.djordjije11.reeledlegacy.model.AuthorAnalyticsEmailRecipient;
import io.github.djordjije11.reeledlegacy.model.AuthorType;
import io.github.djordjije11.reeledlegacy.repository.AuthorRepository;
import io.github.djordjije11.reeledlegacy.repository.AuthorTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final AuthorTypeRepository authorTypeRepository;

    public Long create(String name, Long typeId, String bio, String imageUrl) {
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(typeId, "typeId must not be null");

        logger.info("Creating an author (name: {})...", name);

        final AuthorType authorType = authorTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Author type does not exist (id: %d)".formatted(typeId)));

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

        authorRepository.delete(author);

        logger.info("Author successfully deleted (id: {})", id);
    }

    @Transactional
    public void updateAnalyticsEmailRecipients(Long id, List<String> analyticsEmailRecipients) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(analyticsEmailRecipients, "analyticsEmailRecipients must not be null");

        logger.info("Updating author analytics email recipients (id: {})...", id);

        if (analyticsEmailRecipients.size() > 20) {
            throw new ReeledException("Cannot add more than 20 analytics email recipients");
        }

        if (analyticsEmailRecipients.stream().distinct().count() < analyticsEmailRecipients.size()) {
            throw new ReeledException("Duplicated analytics email recipients are not allowed");
        }

        final Author author = getAuthor(id);

        if (!author.getType().getName().equals("business")) {
            throw new ReeledException("Analytics email recipients can't be updated because the author type is not business");
        }

        final Set<AuthorAnalyticsEmailRecipient> authorAnalyticsEmailRecipients = author.getAnalyticsEmailRecipients();

        final Set<AuthorAnalyticsEmailRecipient> updatedAuthorAnalyticsEmailRecipients = analyticsEmailRecipients.stream()
                .map(email -> authorAnalyticsEmailRecipients.stream()
                        .filter(authorRecipient -> authorRecipient.getEmail().equals(email))
                        .findFirst()
                        .orElseGet(() -> {
                            final AuthorAnalyticsEmailRecipient recipient = new AuthorAnalyticsEmailRecipient();
                            recipient.setEmail(email);
                            recipient.setAuthor(author);
                            return recipient;
                        }))
                .collect(Collectors.toSet());

        authorAnalyticsEmailRecipients.clear();
        authorAnalyticsEmailRecipients.addAll(updatedAuthorAnalyticsEmailRecipients);

        authorRepository.save(author);

        logger.info("Author analytics email recipients successfully updated (id: {})", id);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
