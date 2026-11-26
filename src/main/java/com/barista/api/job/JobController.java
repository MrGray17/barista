package com.barista.api.job;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barista.application.job.GetJob;
import com.barista.application.job.SubmitJob;
import com.barista.job.Job;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final SubmitJob submitJob;
    private final GetJob getJob;

    public JobController(SubmitJob submitJob, GetJob getJob) {
        this.submitJob = Objects.requireNonNull(submitJob);
        this.getJob = Objects.requireNonNull(getJob);
    }

    @PostMapping
    public ResponseEntity<JobResponse> submit(@Valid @RequestBody SubmitJobRequest request) {
        Job job = submitJob.submit(request.type(), request.payload());
        JobResponse jobResponse = JobResponse.from(job) ;
        URI location = URI.create("/api/v1/jobs/" + job.getId());
        return ResponseEntity.created(location).body(jobResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable UUID id) {
        Optional<Job> result = getJob.get(id);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Job job = result.get();

        JobResponse response = JobResponse.from(job) ;
        return ResponseEntity.ok(response);
    }

}