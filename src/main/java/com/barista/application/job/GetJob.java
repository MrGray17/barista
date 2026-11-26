package com.barista.application.job;

import com.barista.job.Job;

import java.util.UUID;
import java.util.Optional;
import java.util.Objects;

public class GetJob {
    private final JobRepository jobRepository ;
    public GetJob (JobRepository jobRepository) {
        this.jobRepository = Objects.requireNonNull(jobRepository) ;
    }

    public Optional <Job> get(UUID id) {
        return jobRepository.findById(id) ;
    }
}