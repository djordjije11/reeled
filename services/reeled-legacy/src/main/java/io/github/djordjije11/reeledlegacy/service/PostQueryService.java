package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.model.PostProjection;
import io.github.djordjije11.reeledlegacy.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;

    public PostProjection get(Long id, Long authorId) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(authorId, "authorId must not be null");

        return postQueryRepository.findByIdAndAuthorId(id, authorId)
                .orElseThrow(() -> new NotFoundException("Post does not exist (id: %d, authorId: %d)".formatted(id, authorId)));
    }
}
