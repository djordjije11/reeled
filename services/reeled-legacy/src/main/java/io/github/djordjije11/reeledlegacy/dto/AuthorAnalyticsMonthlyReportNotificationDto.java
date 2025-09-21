package io.github.djordjije11.reeledlegacy.dto;

import java.time.YearMonth;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
public record AuthorAnalyticsMonthlyReportNotificationDto(Author author, List<EmailRecipient> emailRecipients, Metrics metrics, YearMonth period) {

    public record Author(Long id, String name) {

    }

    public record EmailRecipient(String email) {

    }

    public record Metrics(Long searchAppearancesSum, Double searchAppearancesAvg, Long viewsSum, Double viewsAvg) {

    }
}
