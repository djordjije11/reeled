package io.github.djordjije11.reeled.postmetrics.application;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
import io.github.djordjije11.reeled.commons.exception.NotFoundException;
import io.github.djordjije11.reeled.integration.internal.service.emailnotification.rest.EmailNotificationServiceClient;
import io.github.djordjije11.reeled.postmetrics.domain.AnalyticsQueryServiceClient;
import io.github.djordjije11.reeled.postmetrics.domain.Author;
import io.github.djordjije11.reeled.postmetrics.domain.AuthorRepository;
import io.github.djordjije11.reeled.postmetrics.domain.AuthorSupportRepository;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.ClockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.Duration;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

import static io.github.djordjije11.reeled.config.ResilienceConfiguration.DATA_ACCESS_RETRY_NAME;

/**
 * @author Djordjije Radovic
 */
@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private static final Duration ANALYTICS_MONTHLY_REPORT_PROCESSING_DELAY = Duration.ofHours(14);

    private static final int FETCH_SIZE = 5000;

    private final AuthorRepository authorRepository;

    private final AuthorSupportRepository authorSupportRepository;

    private final AnalyticsQueryServiceClient analyticsQueryServiceClient;

    private final EmailNotificationServiceClient emailNotificationServiceClient;

    private final io.github.resilience4j.retry.Retry dataAccessRetry;

    private final Clock clock;

    public AuthorService(AuthorRepository authorRepository,
                         AuthorSupportRepository authorSupportRepository,
                         AnalyticsQueryServiceClient analyticsQueryServiceClient,
                         EmailNotificationServiceClient emailNotificationServiceClient,
                         RetryRegistry retryRegistry,
                         ClockProvider clockProvider) {
        this.authorRepository = authorRepository;
        this.authorSupportRepository = authorSupportRepository;
        this.analyticsQueryServiceClient = analyticsQueryServiceClient;
        this.emailNotificationServiceClient = emailNotificationServiceClient;
        this.dataAccessRetry = retryRegistry.retry(DATA_ACCESS_RETRY_NAME);
        this.clock = clockProvider.getClock();
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void save(Long id, String name, AuthorType type) {
        Assert.notNull(id, "id must not be null");

        logger.info("Saving author (id: {})", id);

        authorRepository.findById(id).ifPresentOrElse(author -> {
            author.update(name, type);
            authorRepository.save(author);
        }, () -> authorRepository.save(new Author(id, name, type)));

        logger.info("Author successfully saved (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting author (id: {})", id);

        final Author author = getAuthor(id);

        authorRepository.delete(author);

        logger.info("Author successfully deleted (id: {})", id);
    }

    @Retry(name = DATA_ACCESS_RETRY_NAME)
    public void updateAnalyticsEmailRecipients(Long id, List<Author.AnalyticsEmailRecipient> analyticsEmailRecipients) {
        Assert.notNull(id, "id must not be null");

        logger.info("Updating author analytics email recipients (id: {})...", id);

        final Author author = getAuthor(id);

        author.updateAnalyticsEmailRecipients(analyticsEmailRecipients);

        authorRepository.save(author);

        logger.info("Author analytics email recipients successfully updated (id: {})", id);
    }

    public void processAnalyticsMonthlyReports() {
        final YearMonth period = YearMonth.from(ZonedDateTime.now(clock).minus(ANALYTICS_MONTHLY_REPORT_PROCESSING_DELAY).minusMonths(1));

        logger.info("Processing author analytics monthly reports (period: {})...", period);

        List<Long> idsEligibleForAnalyticsMonthlyReport;
        do {
            idsEligibleForAnalyticsMonthlyReport = authorSupportRepository.findAllIdsEligibleForAnalyticsMonthlyReport(period, FETCH_SIZE);
            idsEligibleForAnalyticsMonthlyReport.forEach(id -> {
                try {
                    dataAccessRetry.executeRunnable(() -> processAnalyticsMonthlyReport(id, period));
                } catch (RuntimeException e) {
                    logger.error("Failed to process author analytics monthly report (id: {}, period: {})", id, period, e);
                }
            });
        } while (idsEligibleForAnalyticsMonthlyReport.size() == FETCH_SIZE);

        logger.info("Author analytics monthly reports successfully processed (period: {})", period);
    }

    private void processAnalyticsMonthlyReport(Long id, YearMonth period) {
        logger.info("Processing author analytics monthly report (id: {}, period: {})...", id, period);

        final Author author = getAuthor(id);

        author.processAnalyticsMonthlyReport(period, analyticsQueryServiceClient, emailNotificationServiceClient);

        authorRepository.save(author);

        logger.info("Author analytics monthly report successfully processed (id: {}, period: {})", id, period);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
