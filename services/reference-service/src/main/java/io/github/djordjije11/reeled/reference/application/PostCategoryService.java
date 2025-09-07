package io.github.djordjije11.reeled.reference.application;

import io.github.djordjije11.reeled.reference.domain.PostCategory;
import io.github.djordjije11.reeled.reference.domain.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;

    public void create(String key) {
        final PostCategory postCategory = new PostCategory(key, postCategoryRepository);
        postCategoryRepository.save(postCategory);
    }
}
