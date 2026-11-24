package com.barista.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.time.Instant;
import java.util.Set;
import java.util.IdentityHashMap;

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

    private static Object immutableCopy(Object object,
            Set<Object> visiting) {

        if (object == null ||
                object instanceof String ||
                object instanceof Boolean ||
                object instanceof Integer ||
                object instanceof Double ||
                object instanceof Long ||
                object instanceof Float ||
                object instanceof Byte ||
                object instanceof Short) {

            return object;
        }

        if (object instanceof List<?> list) {
            return immutableListCopy(list, visiting);
        }

        if (object instanceof Map<?, ?> map) {
            return immutableMapCopy(map, visiting);
        }

        throw new IllegalArgumentException(
                "Unsupported payload value type");
    }

    private static List<Object> immutableListCopy(List<?> list,
            Set<Object> visiting) {

        if (visiting.contains(list)) {
            throw new IllegalArgumentException(
                    "Circular reference detected");
        }

        visiting.add(list);

        try {

            List<Object> copyList = new ArrayList<>();

            for (Object element : list) {

                Object copiedElmt = immutableCopy(element, visiting);
                copyList.add(copiedElmt);
            }

            return Collections.unmodifiableList(copyList);
        } finally {
            visiting.remove(list);
        }
    }

    private static Map<String, Object> immutableMapCopy(Map<?, ?> map,
            Set<Object> visiting) {

        if (visiting.contains(map)) {  // we could just verify with .add insted of two ops
            throw new IllegalArgumentException(
                    "Circular reference detected");
        }

        visiting.add(map);

        try {

            Map<String, Object> copyMap = new HashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {

                Object key = entry.getKey();
                Object value = entry.getValue();

                if (!(key instanceof String stringKey)) {
                    throw new IllegalArgumentException(
                            "The Map key must be a String");
                }

                copyMap.put(stringKey, immutableCopy(value, visiting));
            }

            return Collections.unmodifiableMap(copyMap);
        } finally {
            visiting.remove(map);
        }
    }

    private static Map<String, Object> immutableMapCopy( // overloads the existing method
            Map<?, ?> map) {

        Set<Object> visiting = Collections.newSetFromMap(
                new IdentityHashMap<>());

        return immutableMapCopy(map, visiting);
    }

    public Job(
            String type,
            Map<String, Object> payload,
            Instant createdAt) {

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
        Map<String, Object> payloadCopy = immutableMapCopy(payload);

        // Initialize
        this.id = UUID.randomUUID();
        this.status = JobStatus.PENDING;
        this.type = type;
        this.payload = payloadCopy;
        this.attempts = 0;
        this.maxAttempts = maxAttempts;
        this.createdAt = createdAt;
        this.availableAt = createdAt;
    }

    public void start(Instant now) {

        if (now == null) {
            throw new IllegalArgumentException(
                    "Current time cannot be null");
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
    }

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

        this.payload = immutableMapCopy(
                Objects.requireNonNull(payload));

        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.availableAt = Objects.requireNonNull(availableAt);

        if (type.isBlank()) {
            throw new IllegalArgumentException(
                    "Job type cannot be blank");
        }

        if (maxAttempts < 1) {
            throw new IllegalArgumentException(
                    "Invalid max attempts");
        }

        if (attempts < 0 || attempts > maxAttempts) {
            throw new IllegalArgumentException(
                    "Invalid attempts");
        }

        if (status == JobStatus.PENDING && attempts != 0) {
            throw new IllegalArgumentException(
                    "A pending job must never have started");
        }

        if (status != JobStatus.PENDING && attempts == 0) {
            throw new IllegalArgumentException(
                    "Any status other than PENDING requires execution to have started");
        }

        if (status == JobStatus.RETRYING && attempts == maxAttempts) {
            throw new IllegalArgumentException(
                    "A RETRYING job must have remaining attempts");
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