package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.model.PostCategory;
import io.github.djordjije11.reeledlegacy.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostCategoryQueryService {

    private final PostCategoryRepository postCategoryRepository;

    public Set<PostCategory> getAll() {
        return postCategoryRepository.findAll();
    }

    public PostCategory get(Long id) {
        return postCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Post category does not exist (id: %s)".formatted(id)));
    }
}
