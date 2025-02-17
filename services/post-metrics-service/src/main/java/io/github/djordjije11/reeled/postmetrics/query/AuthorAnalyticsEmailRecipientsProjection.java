package io.github.djordjije11.reeled.postmetrics.query;

import io.github.djordjije11.reeled.postmetrics.domain.Author;

import java.util.Collection;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
public record AuthorAnalyticsEmailRecipientsProjection(List<EmailRecipient> analyticsEmailRecipients) {

    AuthorAnalyticsEmailRecipientsProjection(Collection<Author.AnalyticsEmailRecipient> analyticsEmailRecipients) {
        this(analyticsEmailRecipients.stream().map(m -> new EmailRecipient(m.email())).toList());
    }

    public record EmailRecipient(String email) {

    }
}
