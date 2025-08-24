package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.model.AuthorProjection;
import io.github.djordjije11.reeledlegacy.repository.AuthorQueryRepository;
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

        return authorQueryRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new RuntimeException("Author does not exist (id: %d)".formatted(id)));
    }
}
