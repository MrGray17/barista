package com.barista.application.job;

import com.barista.job.Job;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository {

    void save(Job job);

    Optional<Job> findById(UUID id);

}
