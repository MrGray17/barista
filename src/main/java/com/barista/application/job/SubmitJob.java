package com.barista.application.job;

import com.barista.job.Job;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;

public class SubmitJob {

    private final JobRepository jobRepository;
    private final Clock clock;

    public SubmitJob(JobRepository jobRepository, Clock clock) {
        this.jobRepository = Objects.requireNonNull(jobRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    public Job submit(String type, Map<String, Object> payload) {
        Job job = new Job(type, payload, clock.instant());
        jobRepository.save(job);
        return job;
    }
}