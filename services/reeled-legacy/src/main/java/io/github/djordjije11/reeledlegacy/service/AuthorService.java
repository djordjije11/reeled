package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.commons.exception.ReeledException;
import io.github.djordjije11.reeledlegacy.dto.AuthorAnalyticsMonthlyReportNotificationDto;
import io.github.djordjije11.reeledlegacy.model.Author;
import io.github.djordjije11.reeledlegacy.model.AuthorAnalyticsEmailRecipient;
import io.github.djordjije11.reeledlegacy.model.AuthorType;
import io.github.djordjije11.reeledlegacy.model.PostTotalMetricsProjection;
import io.github.djordjije11.reeledlegacy.repository.AuthorRepository;
import io.github.djordjije11.reeledlegacy.repository.AuthorTypeRepository;
import io.github.djordjije11.reeledlegacy.repository.PostDailyPerformanceQueryRepository;
import jakarta.validation.ClockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Djordjije Radovic
 */
@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private static final Duration ANALYTICS_MONTHLY_REPORT_PROCESSING_DELAY = Duration.ofHours(14);

    private static final int FETCH_SIZE = 5000;

    private final AuthorRepository authorRepository;

    private final AuthorTypeRepository authorTypeRepository;

    private final EmailNotificationService emailNotificationService;

    private final TransactionTemplate transactionTemplate;

    private final Clock clock;

    private final PostDailyPerformanceQueryRepository postDailyPerformanceQueryRepository;

    public AuthorService(AuthorRepository authorRepository,
                         AuthorTypeRepository authorTypeRepository,
                         PostDailyPerformanceQueryRepository postDailyPerformanceQueryRepository,
                         EmailNotificationService emailNotificationService,
                         PlatformTransactionManager platformTransactionManager,
                         ClockProvider clockProvider) {
        this.authorRepository = authorRepository;
        this.authorTypeRepository = authorTypeRepository;
        this.postDailyPerformanceQueryRepository = postDailyPerformanceQueryRepository;
        this.emailNotificationService = emailNotificationService;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.clock = clockProvider.getClock();
    }

    public Long create(String name, Long typeId, String bio, String imageUrl) {
        Assert.hasText(name, "name must not be empty");
        Assert.notNull(typeId, "typeId must not be null");

        logger.info("Creating an author (name: {})...", name);

        final AuthorType authorType = authorTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Author type does not exist (id: %d)".formatted(typeId)));

        final Author author = new Author();
        author.setName(name);
        author.setType(authorType);
        author.setBio(bio);
        author.setImageUrl(imageUrl);

        authorRepository.save(author);

        logger.info("Author successfully created (id: {}, name: {})", author.getId(), name);

        return author.getId();
    }

    public void update(Long id, String name, String bio, String imageUrl) {
        Assert.notNull(id, "id must not be null");
        Assert.hasText(name, "name must not be empty");

        logger.info("Updating an author (id: {})...", id);

        final Author author = getAuthor(id);
        author.setName(name);
        author.setBio(bio);
        author.setImageUrl(imageUrl);

        authorRepository.save(author);

        logger.info("Author successfully updated (id: {})", id);
    }

    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        logger.info("Deleting an author (id: {})...", id);

        final Author author = getAuthor(id);

        authorRepository.delete(author);

        logger.info("Author successfully deleted (id: {})", id);
    }

    @Transactional
    public void updateAnalyticsEmailRecipients(Long id, List<String> analyticsEmailRecipients) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(analyticsEmailRecipients, "analyticsEmailRecipients must not be null");

        logger.info("Updating author analytics email recipients (id: {})...", id);

        if (analyticsEmailRecipients.size() > 20) {
            throw new ReeledException("Cannot add more than 20 analytics email recipients");
        }

        if (analyticsEmailRecipients.stream().distinct().count() < analyticsEmailRecipients.size()) {
            throw new ReeledException("Duplicated analytics email recipients are not allowed");
        }

        final Author author = getAuthor(id);

        if (!author.getType().getName().equals("business")) {
            throw new ReeledException("Analytics email recipients can't be updated because the author type is not business");
        }

        final Set<AuthorAnalyticsEmailRecipient> authorAnalyticsEmailRecipients = author.getAnalyticsEmailRecipients();

        final Set<AuthorAnalyticsEmailRecipient> updatedAuthorAnalyticsEmailRecipients = analyticsEmailRecipients.stream()
                .map(email -> authorAnalyticsEmailRecipients.stream()
                        .filter(authorRecipient -> authorRecipient.getEmail().equals(email))
                        .findFirst()
                        .orElseGet(() -> {
                            final AuthorAnalyticsEmailRecipient recipient = new AuthorAnalyticsEmailRecipient();
                            recipient.setEmail(email);
                            recipient.setAuthor(author);
                            return recipient;
                        }))
                .collect(Collectors.toSet());

        authorAnalyticsEmailRecipients.clear();
        authorAnalyticsEmailRecipients.addAll(updatedAuthorAnalyticsEmailRecipients);

        authorRepository.save(author);

        logger.info("Author analytics email recipients successfully updated (id: {})", id);
    }

    public void processAnalyticsMonthlyReports() {
        final YearMonth period = YearMonth.from(ZonedDateTime.now(clock).minus(ANALYTICS_MONTHLY_REPORT_PROCESSING_DELAY).minusMonths(1));

        logger.info("Processing author analytics monthly reports (period: {})...", period);

        List<Long> idsEligibleForAnalyticsMonthlyReport;
        do {
            idsEligibleForAnalyticsMonthlyReport = authorRepository.findAllIdsEligibleForAnalyticsMonthlyReport(period, FETCH_SIZE);
            idsEligibleForAnalyticsMonthlyReport.forEach(id -> {
                try {
                    transactionTemplate.executeWithoutResult(transactionStatus -> processAnalyticsMonthlyReport(id, period));
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

        if (!CollectionUtils.isEmpty(author.getAnalyticsEmailRecipients())) {
            final PostTotalMetricsProjection postMetrics = postDailyPerformanceQueryRepository.searchTotalMetrics(id,
                    period.atDay(1),
                    period.plusMonths(1).atDay(1));

            final AuthorAnalyticsMonthlyReportNotificationDto reportNotification = new AuthorAnalyticsMonthlyReportNotificationDto(new AuthorAnalyticsMonthlyReportNotificationDto.Author(
                    id,
                    author.getName()),
                    author.getAnalyticsEmailRecipients()
                            .stream()
                            .map(analyticsEmailRecipient -> new AuthorAnalyticsMonthlyReportNotificationDto.EmailRecipient(analyticsEmailRecipient.getEmail()))
                            .toList(),
                    new AuthorAnalyticsMonthlyReportNotificationDto.Metrics(postMetrics.searchAppearancesSum(),
                            postMetrics.searchAppearancesAvg(),
                            postMetrics.viewsSum(),
                            postMetrics.viewsAvg()),
                    period);

            emailNotificationService.sendAuthorAnalyticsMonthlyReportNotification(reportNotification);
        }

        author.setAnalyticsMonthlyReportLastProcessedPeriod(period);

        authorRepository.save(author);

        logger.info("Author analytics monthly report successfully processed (id: {}, period: {})", id, period);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author does not exist (id: %d)".formatted(id)));
    }
}
