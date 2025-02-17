package io.github.djordjije11.reeled.postmetrics.domain.exception;

import io.github.djordjije11.reeled.commons.exception.ReeledDomainException;

public class AuthorAnalyticsMonthlyReportNotProcessableException extends ReeledDomainException {

    public AuthorAnalyticsMonthlyReportNotProcessableException(String message) {
        super(message);
    }
}
