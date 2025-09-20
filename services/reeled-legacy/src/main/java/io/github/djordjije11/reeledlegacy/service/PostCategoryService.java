package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.model.PostCategory;
import io.github.djordjije11.reeledlegacy.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;

    public Long create(String name) {
        Assert.hasText(name, "name must be provided");

        final PostCategory postCategory = new PostCategory();
        postCategory.setName(name);

        postCategoryRepository.save(postCategory);

        return postCategory.getId();
    }
}
