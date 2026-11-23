package com.barista.job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.time.Instant;

public class Job {

    private final UUID id;
    private JobStatus status;
    private final String type;
    private final Map<String, Object> payload;
    private int attempts;
    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private final int maxAttempts;
    private final Instant createdAt;
    private Instant availableAt;

    public Job(String type, Map<String, Object> payload, Instant createdAt) {
        this(type, payload, createdAt, DEFAULT_MAX_ATTEMPTS);
    }

    public Job(
            String type,
            Map<String, Object> payload,
            Instant createdAt,
            int maxAttempts) {
        // Validation
        Objects.requireNonNull(type, "Job type cannot be null");
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(createdAt, "Creation time cannot be null");

        if (type.isBlank()) {
            throw new IllegalArgumentException("Job type cannot be blank");
        }

        if (maxAttempts < 1) {
            throw new IllegalArgumentException(
                    "Maximum attempts must be at least 1");
        }

        // Prepare
        Map<String, Object> payloadCopy = Collections.unmodifiableMap(new HashMap<>(payload));

        // Initialize
        this.id = UUID.randomUUID();
        this.status = JobStatus.PENDING;
        this.type = type;
        this.payload = payloadCopy;
        this.attempts = 0;
        this.maxAttempts = maxAttempts; // total number of executions, including the first attempt, not the number of
                                        // retries after the first execution.
        this.createdAt = createdAt;
        this.availableAt = createdAt;
    }

    public void start(Instant now) {
        if (now == null) {
            throw new IllegalArgumentException(
                    "Current time cannot be null");
            // or Objects.requireNonNull(now, "Current time cannot be null");
        }
        if (this.status != JobStatus.PENDING
                && this.status != JobStatus.RETRYING) {
            throw new IllegalStateException(
                    "Job cannot be started from its current state");
        }

        if (this.attempts >= this.maxAttempts) {
            throw new IllegalStateException(
                    "Maximum attempts reached");
        }

        if (now.isBefore(this.availableAt)) {
            throw new IllegalStateException(
                    "Job is not available for execution yet");
        }

        this.attempts++;
        this.status = JobStatus.RUNNING;
    }

    public void succeed() {
        if (this.status != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only running jobs can succeed");
        }
        this.status = JobStatus.SUCCEEDED;
    }

    public boolean hasAttemptsRemaining() {
        return this.attempts < this.maxAttempts;
    }

    public void scheduleRetry(Instant nextAvailableAt) {
        Objects.requireNonNull(
                nextAvailableAt,
                "Next available time cannot be null");

        if (this.status != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only running jobs can be scheduled for retry");
        }

        if (!hasAttemptsRemaining()) {
            throw new IllegalStateException(
                    "No attempts remaining");
        }

        this.availableAt = nextAvailableAt;
        this.status = JobStatus.RETRYING;
    }

    public void markDead() {
        if (this.status != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only running jobs can be marked dead");
        }

        this.status = JobStatus.DEAD;
    }

    public JobStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getAvailableAt() {
        return availableAt;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> getPayload() {
        return payload;
    };

    public int getMaxAttempts() {
        return maxAttempts;
    }

    private Job(
            UUID id,
            String type,
            Map<String, Object> payload,
            JobStatus status,
            int attempts,
            int maxAttempts,
            Instant createdAt,
            Instant availableAt) {

        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.payload = Collections.unmodifiableMap(
                new HashMap<>(Objects.requireNonNull(payload)));
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.availableAt = Objects.requireNonNull(availableAt);

        if (type.isBlank()) {
            throw new IllegalArgumentException("Job type cannot be blank");
        }

        if (maxAttempts < 1) {
            throw new IllegalArgumentException("Invalid max attempts");
        }

        if (attempts < 0 || attempts > maxAttempts) {
            throw new IllegalArgumentException("Invalid attempts");
        }

        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
    }

    public static Job restore(
            UUID id,
            String type,
            Map<String, Object> payload,
            JobStatus status,
            int attempts,
            int maxAttempts,
            Instant createdAt,
            Instant availableAt) {

        return new Job(
                id,
                type,
                payload,
                status,
                attempts,
                maxAttempts,
                createdAt,
                availableAt);
    }

}
