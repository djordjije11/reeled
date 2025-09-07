package io.github.djordjije11.reeled.reference.query;

import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostCategoryQueryService {

    private final PostCategoryQueryRepository postCategoryQueryRepository;

    public List<PostCategoryProjection> getAll() {
        return postCategoryQueryRepository.findAll();
    }

    public PostCategoryProjection get(String key) {
        return postCategoryQueryRepository.findByKey(key).orElseThrow(() -> new NotFoundException("Post category does not exist (key: %s)".formatted(key)));
    }
}
