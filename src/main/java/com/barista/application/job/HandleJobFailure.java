package com.barista.application.job;

import com.barista.job.Job;
import com.barista.job.JobStatus;
import com.barista.job.RetryPolicy;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class HandleJobFailure {

    private final FailureClassifier failureClassifier;
    private final RetryPolicy retryPolicy;
    private final Clock clock;

    public HandleJobFailure(FailureClassifier failureClassifier,
            RetryPolicy retryPolicy,
            Clock clock) {
        Objects.requireNonNull(failureClassifier,
                "The Failure classifier cannot be null");
        Objects.requireNonNull(retryPolicy,
                "The retry policy cannot be null");
        Objects.requireNonNull(clock,
                "Time cannot be null");

        this.failureClassifier = failureClassifier;
        this.retryPolicy = retryPolicy;
        this.clock = clock;
    }

    public void handle(Job job, Exception failure) {
        Objects.requireNonNull(job, "The job cannot be null");
        Objects.requireNonNull(failure, "The failure cannot be null");

        if (job.getStatus() != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only running jobs can be handled");
        }

        if (!failureClassifier.isRetryable(failure)) {
            job.markDead();
            return;
        }

        if (!job.hasAttemptsRemaining()) {
            job.markDead();
            return;
        }

        Duration duration = retryPolicy.calculateDelay(job.getAttempts());
        job.scheduleRetry(clock.instant().plus(duration));
    }

}