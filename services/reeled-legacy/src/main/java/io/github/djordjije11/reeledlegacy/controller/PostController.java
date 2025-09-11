package io.github.djordjije11.reeledlegacy.controller;

import io.github.djordjije11.reeledlegacy.dto.PostCreateDto;
import io.github.djordjije11.reeledlegacy.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/authors/{authorId}/posts")
    @Operation(description = "Creates a post for an author")
    @ApiResponse(responseCode = "201", description = "Post created")
    public ResponseEntity<Void> create(@PathVariable Long authorId, @RequestBody @Valid PostCreateDto postCreateDto) {
        final Long postId = postService.create(authorId,
                postCreateDto.categoryId(),
                postCreateDto.description(),
                postCreateDto.duration(),
                postCreateDto.monetized(),
                postCreateDto.title(),
                postCreateDto.videoUrl());

        return ResponseEntity.created(URI.create("/" + postId.toString())).build();
    }
}
