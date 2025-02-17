package io.github.djordjije11.reeled.integration.internal.service.emailnotification.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Component
public class EmailNotificationServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationServiceClient.class);

    public void sendAuthorAnalyticsMonthlyReportNotification(AuthorAnalyticsMonthlyReportNotificationDto authorAnalyticsMonthlyReportNotificationDto) {
        Assert.notNull(authorAnalyticsMonthlyReportNotificationDto, "authorAnalyticsMonthlyReportNotificationDto must be provided");

        logger.info("Sending author analytics monthly report notification (authorAnalyticsMonthlyReportNotificationDto: {}...",
                authorAnalyticsMonthlyReportNotificationDto);

        // Calling service for sending email notification

        logger.info("Author analytics monthly report notification successfully sent (authorAnalyticsMonthlyReportNotificationDto: {})",
                authorAnalyticsMonthlyReportNotificationDto);
    }
}
