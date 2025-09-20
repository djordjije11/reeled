package io.github.djordjije11.reeledlegacy.model;

import java.util.Set;

/**
 * @author Djordjije Radovic
 */
public record AuthorAnalyticsEmailRecipientsProjection(Set<AnalyticsEmailRecipient> analyticsEmailRecipients) {

    public record AnalyticsEmailRecipient(Long id, String email) {

    }
}
