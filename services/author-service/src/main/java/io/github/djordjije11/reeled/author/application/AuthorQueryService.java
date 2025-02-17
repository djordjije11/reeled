package io.github.djordjije11.reeled.author.application;

import io.github.djordjije11.reeled.author.query.AuthorProjection;
import io.github.djordjije11.reeled.author.query.AuthorQueryRepository;
import io.github.djordjije11.reeled.commons.exception.NotFoundException;
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

    public AuthorProjection get(Long id) {
        Assert.notNull(id, "id must not be null");

        return authorQueryRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
