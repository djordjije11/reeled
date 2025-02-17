package io.github.djordjije11.reeled.postmetrics.infra.web.rest;

import io.github.djordjije11.reeled.postmetrics.application.AuthorQueryService;
import io.github.djordjije11.reeled.postmetrics.application.AuthorService;
import io.github.djordjije11.reeled.postmetrics.domain.Author;
import io.github.djordjije11.reeled.postmetrics.query.AuthorAnalyticsEmailRecipientsProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Djordjije Radovic
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
@Tag(name = "Author")
@Validated
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorQueryService authorQueryService;

    @GetMapping("/{authorId}/analytics-email-recipients")
    @Operation(description = "Returns analytics email recipients for an author")
    @ApiResponse(responseCode = "200", description = "Author analytics email recipients returned")
    public ResponseEntity<AuthorAnalyticsEmailRecipientsProjection> getAnalyticsEmailRecipients(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorQueryService.getAnalyticsEmailRecipients(authorId));
    }

    @PutMapping("/{authorId}/analytics-email-recipients")
    @Operation(description = "Updates author analytics email recipients")
    @ApiResponse(responseCode = "204", description = "Author analytics email recipients updated")
    public ResponseEntity<Void> updateAnalyticsEmailRecipients(@PathVariable Long authorId,
                                                               @RequestBody @Valid AnalyticsEmailRecipientsUpdateDto analyticsEmailRecipientsUpdateDto) {
        authorService.updateAnalyticsEmailRecipients(authorId,
                analyticsEmailRecipientsUpdateDto.analyticsEmailRecipients().stream().map(m -> new Author.AnalyticsEmailRecipient(m.email())).toList());

        return ResponseEntity.noContent().build();
    }
}
