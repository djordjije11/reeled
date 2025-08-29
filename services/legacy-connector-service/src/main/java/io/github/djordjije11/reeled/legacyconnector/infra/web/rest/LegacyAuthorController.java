package io.github.djordjije11.reeled.legacyconnector.infra.web.rest;

import io.github.djordjije11.reeled.legacyconnector.application.AuthorLegacyAuthorSyncEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/v1/legacy-authors")
@Tag(name = "Legacy Author")
@Validated
public class LegacyAuthorController {

    private final AuthorLegacyAuthorSyncEntryService authorLegacyAuthorSyncEntryService;

    @PostMapping
    @Operation(description = "Creates a legacy author")
    @ApiResponse(responseCode = "201", description = "Legacy author created")
    public ResponseEntity<Void> create(@RequestBody @Valid AuthorCreateDto authorCreateDto) {
        final Long authorId = authorLegacyAuthorSyncEntryService.create(authorCreateDto.name(),
                authorCreateDto.type(),
                authorCreateDto.bio(),
                authorCreateDto.imageUrl());
        return ResponseEntity.created(URI.create("/" + authorId.toString())).build();
    }
}
