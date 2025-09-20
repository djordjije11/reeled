package io.github.djordjije11.reeledlegacy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author Djordjije Radovic
 */
public record AnalyticsEmailRecipientsUpdateDto(@NotNull List<@Valid @NotNull AnalyticsEmailRecipientDto> analyticsEmailRecipients) {

    public record AnalyticsEmailRecipientDto(@NotBlank @Email @Size(max = 255) String email) {

    }
}
