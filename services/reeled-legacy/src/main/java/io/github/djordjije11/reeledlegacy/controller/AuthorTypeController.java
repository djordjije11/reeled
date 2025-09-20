package io.github.djordjije11.reeledlegacy.controller;

import io.github.djordjije11.reeledlegacy.model.AuthorType;
import io.github.djordjije11.reeledlegacy.repository.AuthorTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Author Type")
public class AuthorTypeController {

    private final AuthorTypeRepository authorTypeRepository;

    @GetMapping("/author-types")
    @Operation(description = "Returns all author types")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Set<AuthorType>> get() {
        return ResponseEntity.ok(authorTypeRepository.findAll());
    }
}
