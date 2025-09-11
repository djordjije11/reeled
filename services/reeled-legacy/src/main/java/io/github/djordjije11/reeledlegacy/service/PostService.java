package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.model.Author;
import io.github.djordjije11.reeledlegacy.model.Post;
import io.github.djordjije11.reeledlegacy.model.PostCategory;
import io.github.djordjije11.reeledlegacy.repository.AuthorRepository;
import io.github.djordjije11.reeledlegacy.repository.PostCategoryRepository;
import io.github.djordjije11.reeledlegacy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    private final PostCategoryRepository postCategoryRepository;

    public Long create(Long authorId, Long categoryId, String description, Duration duration, Boolean monetized, String title, String videoUrl) {
        Assert.notNull(authorId, "authorId must not be null");
        Assert.notNull(categoryId, "categoryId must not be null");
        Assert.hasText(title, "title must be provided");

        logger.info("Creating a post (authorId: {}, title: {})...", authorId, title);

        final Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(authorId)));
        final PostCategory category = postCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Post category does not exist (id: %d)".formatted(categoryId)));

        final Post post = new Post();
        post.setAuthor(author);
        post.setCategory(category);
        post.setDescription(description);
        post.setDuration(duration);
        post.setMonetized(monetized);
        post.setTitle(title);
        post.setVideoUrl(videoUrl);

        postRepository.save(post);

        logger.info("Post successfully created (id: {}, authorId: {}, title: {})", post.getId(), authorId, title);

        return post.getId();
    }
}
