package com.barista.application.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.barista.job.Job;
import com.barista.job.JobStatus;
import com.barista.job.RetryPolicy;
import com.barista.job.RetryableJobException;

public class HandleJobFailureTest {

    @Test
    public void retryableFailureWithAttemptsRemainingSchedulesRetry() {

        FailureClassifier failureClassifier = new DefaultFailureClassifier();

        RetryPolicy retryPolicy = new RetryPolicy(Duration.ofSeconds(2), Duration.ofMinutes(1));

        Instant now = Instant.parse("2026-11-17T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);

        HandleJobFailure handleFailure = new HandleJobFailure(failureClassifier,
                retryPolicy,
                clock);

        Job job = new Job("send-email",
                Map.of(),
                now,
                3);

        job.start(now);
        RetryableJobException excep = new RetryableJobException("An exception occured");

        handleFailure.handle(job, excep);

        assertEquals(JobStatus.RETRYING, job.getStatus());
        assertEquals(1, job.getAttempts());
        assertEquals(now.plusSeconds(2), job.getAvailableAt());
    }

    @Test
    public void aNonRetryableFailureMarksTheJobDead() {
        Instant now = Instant.parse("2026-11-17T12:00:00Z");
        Job job = new Job("send-email", Map.of(), now, 3);

        FailureClassifier failureClassifier = new DefaultFailureClassifier();
        RetryPolicy retryPolicy = new RetryPolicy(Duration.ofSeconds(2), Duration.ofMinutes(1));
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);

        HandleJobFailure handleFailure = new HandleJobFailure(failureClassifier,
                retryPolicy,
                clock);

        Exception failure = new Exception("Invalid Job Payload");

        job.start(now);
        handleFailure.handle(job, failure);

        assertEquals(JobStatus.DEAD, job.getStatus());
        assertEquals(1, job.getAttempts());

    }

    @Test
    public void retryableFailureWithNoAttemptsRemainingMarksJobDead() {

        FailureClassifier failureClassifier = new DefaultFailureClassifier();

        RetryPolicy retryPolicy = new RetryPolicy(Duration.ofSeconds(2), Duration.ofMinutes(1));

        Instant now = Instant.parse("2026-11-17T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);

        HandleJobFailure handleFailure = new HandleJobFailure(failureClassifier,
                retryPolicy,
                clock);

        Job job = new Job("send-email",
                Map.of(),
                now,
                1);

        job.start(now);
        RetryableJobException excep = new RetryableJobException("An exception occured");

        handleFailure.handle(job, excep);

        assertEquals(JobStatus.DEAD, job.getStatus());
        assertEquals(1, job.getAttempts());
    }

    @Test
    public void nonRunningJobCannotBeHandled() {
        FailureClassifier failureClassifier = new DefaultFailureClassifier();

        RetryPolicy retryPolicy = new RetryPolicy(Duration.ofSeconds(2), Duration.ofMinutes(1));

        Instant now = Instant.parse("2026-11-17T12:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);

        HandleJobFailure handleFailure = new HandleJobFailure(failureClassifier,
                retryPolicy,
                clock);

        Job job = new Job("send-email",
                Map.of(),
                now,
                1);

        RetryableJobException failure = new RetryableJobException("An exception occured");

        assertThrows(IllegalStateException.class,
                () -> handleFailure.handle(job, failure));

        assertEquals(JobStatus.PENDING, job.getStatus());
        assertEquals(0, job.getAttempts());
    }

}
