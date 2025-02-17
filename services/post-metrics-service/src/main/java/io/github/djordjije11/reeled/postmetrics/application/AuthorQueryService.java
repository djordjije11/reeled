package io.github.djordjije11.reeled.postmetrics.application;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.postmetrics.query.AuthorAnalyticsEmailRecipientsProjection;
import io.github.djordjije11.reeled.postmetrics.query.AuthorQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class AuthorQueryService {

    private final AuthorQueryRepository authorQueryRepository;

    public AuthorAnalyticsEmailRecipientsProjection getAnalyticsEmailRecipients(Long id) {
        Assert.notNull(id, "id must not be null");

        return authorQueryRepository.findAnalyticsEmailRecipientsById(id)
                .orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
