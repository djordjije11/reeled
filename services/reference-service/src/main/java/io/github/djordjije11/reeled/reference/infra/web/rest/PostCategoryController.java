package io.github.djordjije11.reeled.reference.infra.web.rest;

import io.github.djordjije11.reeled.reference.application.PostCategoryService;
import io.github.djordjije11.reeled.reference.query.PostCategoryProjection;
import io.github.djordjije11.reeled.reference.query.PostCategoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Post Category")
@Validated
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    private final PostCategoryQueryService postCategoryQueryService;

    @PostMapping("/post-categories")
    @Operation(description = "Creates a post category")
    @ApiResponse(responseCode = "201", description = "Post category created")
    public ResponseEntity<Void> create(@RequestBody @Valid PostCategoryCreateDto postCategoryCreateDto) {
        postCategoryService.create(postCategoryCreateDto.key());
        return ResponseEntity.created(URI.create("/" + postCategoryCreateDto.key())).build();
    }

    @GetMapping("/post-categories/{postCategoryKey}")
    @Operation(description = "Returns a post category")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<PostCategoryProjection> get(@PathVariable String postCategoryKey) {
        return ResponseEntity.ok(postCategoryQueryService.get(postCategoryKey));
    }

    @GetMapping("/post-categories")
    @Operation(description = "Returns all post categories")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Set<PostCategoryProjection>> get() {
        return ResponseEntity.ok(postCategoryQueryService.getAll());
    }
}
