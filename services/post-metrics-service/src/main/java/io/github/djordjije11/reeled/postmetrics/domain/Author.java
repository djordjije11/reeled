package io.github.djordjije11.reeled.postmetrics.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.integration.internal.service.emailnotification.rest.AuthorAnalyticsMonthlyReportNotificationDto;
import io.github.djordjije11.reeled.integration.internal.service.emailnotification.rest.EmailNotificationServiceClient;
import io.github.djordjije11.reeled.postmetrics.domain.exception.AuthorAnalyticsEmailRecipientsNotUpdatableException;
import io.github.djordjije11.reeled.postmetrics.domain.exception.AuthorAnalyticsMonthlyReportNotProcessableException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

/**
 * @author Djordjije Radovic
 */
@Getter
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pm_author")
public class Author {

    @Id
    private Long id;

    private String name;

    private AuthorType type;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<AnalyticsEmailRecipient> analyticsEmailRecipients;

    private YearMonth analyticsMonthlyReportLastProcessedPeriod;

    @Version
    private Long version;

    public Author(Long id, String name, AuthorType type) {
        Assert.notNull(id, "id must not be null");
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(type, "type must not be null");

        this.id = id;
        this.name = name;
        this.type = type;
        this.analyticsEmailRecipients = Collections.emptyList();
    }

    public void update(String name, AuthorType type) {
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(type, "type must not be null");

        if (this.name.equals(name) && this.type == type) {
            return;
        }

        this.name = name;
        this.type = type;
    }

    public void updateAnalyticsEmailRecipients(List<AnalyticsEmailRecipient> analyticsEmailRecipients) {
        Assert.notNull(analyticsEmailRecipients, "analyticsEmailRecipients must not be null");

        if (type != AuthorType.BUSINESS) {
            throw new AuthorAnalyticsEmailRecipientsNotUpdatableException("Analytics email recipients can't be updated because the author type is not business");
        }

        if (analyticsEmailRecipients.size() > 20) {
            throw new AuthorAnalyticsEmailRecipientsNotUpdatableException("Cannot add more than 20 analytics email recipients");
        }

        if (analyticsEmailRecipients.stream().distinct().count() < analyticsEmailRecipients.size()) {
            throw new AuthorAnalyticsEmailRecipientsNotUpdatableException("Duplicated analytics email recipients are not allowed");
        }

        if (this.analyticsEmailRecipients.equals(analyticsEmailRecipients)) {
            return;
        }

        this.analyticsEmailRecipients = analyticsEmailRecipients;
    }

    public void processAnalyticsMonthlyReport(YearMonth period,
                                              AnalyticsQueryServiceClient analyticsQueryServiceClient,
                                              EmailNotificationServiceClient emailNotificationServiceClient) {

        if (type != AuthorType.BUSINESS) {
            throw new AuthorAnalyticsMonthlyReportNotProcessableException(
                    "Author analytics monthly report is not processable because author type is not business (id: %d)".formatted(id));
        }

        if (analyticsMonthlyReportLastProcessedPeriod != null && !period.isAfter(analyticsMonthlyReportLastProcessedPeriod)) {
            throw new AuthorAnalyticsMonthlyReportNotProcessableException(
                    "Author analytics monthly report is not processable because it was already processed for the period (id: %d, period: %s)".formatted(id,
                            period));
        }

        if (!CollectionUtils.isEmpty(analyticsEmailRecipients)) {
            final AuthorAnalyticsMonthlyReportNotificationDto reportNotification = createAnalyticsMonthlyReportNotification(period,
                    analyticsQueryServiceClient);
            emailNotificationServiceClient.sendAuthorAnalyticsMonthlyReportNotification(reportNotification);
        }

        analyticsMonthlyReportLastProcessedPeriod = period;
    }

    private AuthorAnalyticsMonthlyReportNotificationDto createAnalyticsMonthlyReportNotification(YearMonth period,
                                                                                                 AnalyticsQueryServiceClient analyticsQueryServiceClient) {
        final PostMonthlyMetricsProjection postMonthlyMetrics = analyticsQueryServiceClient.getPostMonthlyMetricsByAuthor(id, period);

        return new AuthorAnalyticsMonthlyReportNotificationDto(new AuthorAnalyticsMonthlyReportNotificationDto.Author(id, name),
                analyticsEmailRecipients.stream()
                        .map(analyticsEmailRecipient -> new AuthorAnalyticsMonthlyReportNotificationDto.EmailRecipient(analyticsEmailRecipient.email))
                        .toList(),
                new AuthorAnalyticsMonthlyReportNotificationDto.Metrics(postMonthlyMetrics.searchAppearancesSum(),
                        postMonthlyMetrics.searchAppearancesAvg(),
                        postMonthlyMetrics.viewsSum(),
                        postMonthlyMetrics.viewsAvg()),
                period);
    }

    public record AnalyticsEmailRecipient(String email) {

        public AnalyticsEmailRecipient(String email) {
            Assert.hasText(email, "email must be provided");

            this.email = email.toLowerCase().trim();
        }
    }
}
