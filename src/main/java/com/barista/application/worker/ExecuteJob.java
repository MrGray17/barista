package com.barista.application.worker;

import com.barista.application.job.HandleJobFailure;
import com.barista.job.Job;
import com.barista.job.JobStatus;

import java.util.Objects;
import java.util.Optional;

public class ExecuteJob {

    private final HandlerRegistry handlerRegistry;
    private final HandleJobFailure handleJobFailure;

    public ExecuteJob(
            HandlerRegistry handlerRegistry,
            HandleJobFailure handleJobFailure) {

        this.handlerRegistry = Objects.requireNonNull(handlerRegistry);
        this.handleJobFailure = Objects.requireNonNull(handleJobFailure);
    }

    public void execute(Job job) {

        Objects.requireNonNull(job, "Job cannot be null");

        if (job.getStatus() != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only RUNNING jobs can be executed");
        }

        Optional<JobHandler> result = handlerRegistry.findHandler(job.getType());

        if (result.isEmpty()) {
            handleJobFailure.handle(job, new IllegalArgumentException(
                    "Unsupported job type: " + job.getType()));
            return;
        }

        JobHandler handler = result.get();

        try {
            handler.execute(job);
        } catch (Exception failure) {
            handleJobFailure.handle(job, failure);
            return;
        }

        job.succeed();
    }
}