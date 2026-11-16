package com.barista.job;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JobTest {

    @Test
    void startingPendingJobChangesStatusAndIncrementsAttempts() {
        // Arrange
        Job job = new Job("email.send" ,  Map.of() , Instant.now());

        // Act
        job.start(Instant.now());

        // Assert
        assertEquals(JobStatus.RUNNING, job.getStatus());
        assertEquals(1, job.getAttempts());
    }

    @Test
    void startingAlreadyRunningJobThrowsAndPreservesState() {
        Job job = new Job("email.send" ,  Map.of() , Instant.now());

        job.start(Instant.now());

        assertThrows(IllegalStateException.class, () -> job.start(Instant.now()));

        assertEquals(JobStatus.RUNNING, job.getStatus());
        assertEquals(1, job.getAttempts());
    }

    @Test
    void failedJobWaitsUntilRetryTimeBeforeStartingAgain() {
        Job job = new Job("email.send", Map.of() , Instant.now());
        Instant firstAttempt = Instant.now();
        Instant retryAt = Instant.now().plusSeconds(30);
        // failure schedules another attempt
        job.start(firstAttempt);
        job.scheduleRetry(retryAt);;
        assertEquals(JobStatus.RETRYING, job.getStatus());
        assertEquals(1, job.getAttempts());

        // a job cannot execute before its scheduled time
        assertThrows(IllegalStateException.class,
                () -> job.start(retryAt.minusSeconds(2)));

        // a job cannot execute again once its that time arrives
        job.start(retryAt);
        assertThrows(IllegalStateException.class,
                () -> job.start(retryAt));
    }

    @Test
    void jobBecomesDeadAfterMaximumAttempts() {
        Job job = new Job("email.send" ,  Map.of() , Instant.now());
        Instant firstRetryAt = Instant.now().plusSeconds(1);
        Instant secondRetryAt = firstRetryAt.plusSeconds(30);
        job.start(Instant.now());
        job.scheduleRetry(firstRetryAt);

        job.start(firstRetryAt);
        job.scheduleRetry(secondRetryAt);

        job.start(secondRetryAt);
        job.markDead(); 
        assertEquals(JobStatus.DEAD,
                job.getStatus());
        assertEquals(3, job.getAttempts());
    }

}