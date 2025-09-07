package io.github.djordjije11.reeled.reference.infra.web.rest;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.reference.query.AuthorTypeProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Author Type")
public class AuthorTypeController {

    @GetMapping("/author-types")
    @Operation(description = "Returns all author types")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Set<AuthorTypeProjection>> get() {
        final Set<AuthorTypeProjection> authorTypes = Arrays.stream(AuthorType.values())
                .map(AuthorType::getValue)
                .map(AuthorTypeProjection::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(authorTypes);
    }
}
