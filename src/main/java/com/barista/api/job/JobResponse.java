package com.barista.api.job;

import com.barista.job.JobStatus;

import java.time.Instant;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String type,
        JobStatus status,
        Instant createdAt
) {
}
