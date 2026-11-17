package com.barista.application.job;

import com.barista.job.RetryableJobException;

public class DefaultFailureClassifier implements FailureClassifier {

    @Override
    public boolean isRetryable(Exception failure) {
        return failure instanceof RetryableJobException;
    }
}
