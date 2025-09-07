package io.github.djordjije11.reeled.post.infra.web.rest;

import io.github.djordjije11.reeled.post.application.PostQueryService;
import io.github.djordjije11.reeled.post.application.PostService;
import io.github.djordjije11.reeled.post.query.PostProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Post")
@Validated
public class PostController {

    private final PostService postService;

    private final PostQueryService postQueryService;

    @PostMapping("/authors/{authorId}/posts")
    @Operation(description = "Creates a post for an author")
    @ApiResponse(responseCode = "201", description = "Post created")
    public ResponseEntity<Void> create(@PathVariable Long authorId, @RequestBody @Valid PostCreateDto postCreateDto) {
        final Long postId = postService.create(authorId,
                postCreateDto.categoryKey(),
                postCreateDto.description(),
                postCreateDto.duration(),
                postCreateDto.monetized(),
                postCreateDto.title(),
                postCreateDto.videoUrl());

        return ResponseEntity.created(URI.create("/" + postId.toString())).build();
    }

    @PutMapping("/authors/{authorId}/posts/{postId}")
    @Operation(description = "Updates a post for an author")
    @ApiResponse(responseCode = "204", description = "Post updated")
    public ResponseEntity<Void> update(@PathVariable Long authorId, @PathVariable Long postId, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        postService.update(postId, authorId, postUpdateDto.categoryKey(), postUpdateDto.description(), postUpdateDto.title());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/authors/{authorId}/posts/{postId}")
    @Operation(description = "Deletes a post for an author")
    @ApiResponse(responseCode = "204", description = "Post deleted")
    public ResponseEntity<Void> delete(@PathVariable Long authorId, @PathVariable Long postId) {
        postService.delete(postId, authorId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors/{authorId}/posts/{postId}")
    @Operation(description = "Returns a post for an author")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<PostProjection> get(@PathVariable Long authorId, @PathVariable Long postId) {
        return ResponseEntity.ok(postQueryService.get(postId, authorId));
    }
}
