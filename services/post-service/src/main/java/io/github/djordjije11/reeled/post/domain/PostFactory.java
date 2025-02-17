package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class PostFactory {

    private final PostRepository postRepository;

    public Post create(Long authorId, PostCategory category, String description, Duration duration, Boolean monetized, String title, String videoUrl) {
        return new Post(postRepository.nextId(), authorId, category, description, duration, monetized, title, videoUrl);
    }
}
