package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.model.AuthorAnalyticsEmailRecipientsProjection;
import io.github.djordjije11.reeledlegacy.model.AuthorAnalyticsEmailRecipientsProjection.AnalyticsEmailRecipient;
import io.github.djordjije11.reeledlegacy.model.AuthorProjection;
import io.github.djordjije11.reeledlegacy.repository.AuthorQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class AuthorQueryService {

    private final AuthorQueryRepository authorQueryRepository;

    public AuthorProjection get(Long id) {
        Assert.notNull(id, "id must not be null");

        return authorQueryRepository.findById(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }

    @Transactional(readOnly = true)
    public AuthorAnalyticsEmailRecipientsProjection getAnalyticsEmailRecipients(Long id) {
        Assert.notNull(id, "id must not be null");

        return authorQueryRepository.findAnalyticsEmailRecipientsById(id)
                .map(author -> new AuthorAnalyticsEmailRecipientsProjection(author.getAnalyticsEmailRecipients()
                        .stream()
                        .map(recipient -> new AnalyticsEmailRecipient(recipient.getId(), recipient.getEmail()))
                        .collect(Collectors.toSet())))
                .orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
