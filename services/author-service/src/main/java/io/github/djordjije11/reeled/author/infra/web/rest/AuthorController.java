package io.github.djordjije11.reeled.author.infra.web.rest;

import io.github.djordjije11.reeled.author.application.AuthorQueryService;
import io.github.djordjije11.reeled.author.application.AuthorService;
import io.github.djordjije11.reeled.author.query.AuthorProjection;
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
@Tag(name = "Author")
@Validated
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorQueryService authorQueryService;

    @PostMapping("/authors")
    @Operation(description = "Creates an author")
    @ApiResponse(responseCode = "201", description = "Author created")
    public ResponseEntity<Void> create(@RequestBody @Valid AuthorCreateDto authorCreateDto) {
        final Long authorId = authorService.create(authorCreateDto.id(),
                authorCreateDto.name(),
                authorCreateDto.type(),
                authorCreateDto.bio(),
                authorCreateDto.imageUrl());
        return ResponseEntity.created(URI.create("/" + authorId.toString())).build();
    }

    @PutMapping("/authors/{authorId}")
    @Operation(description = "Updates an author")
    @ApiResponse(responseCode = "204", description = "Author updated")
    public ResponseEntity<Void> update(@PathVariable Long authorId, @RequestBody @Valid AuthorUpdateDto authorUpdateDto) {
        authorService.update(authorId, authorUpdateDto.name(), authorUpdateDto.bio(), authorUpdateDto.imageUrl());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/authors/{authorId}")
    @Operation(description = "Deletes an author")
    @ApiResponse(responseCode = "204", description = "Author deleted")
    public ResponseEntity<Void> delete(@PathVariable Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors/{authorId}")
    @Operation(description = "Returns an author")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<AuthorProjection> get(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorQueryService.get(authorId));
    }
}
