package com.barista.infrastructure.database;

import com.barista.application.job.JobRepository;
import com.barista.job.Job;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class JdbcJobRepositoryTest {
        private final JobRepository jobrepository;
        private final JdbcTemplate jdbctemplate;

        JdbcJobRepositoryTest(
                        @Autowired JobRepository jobrepository,
                        @Autowired JdbcTemplate jdbcTemplate) {

                this.jobrepository = jobrepository;
                this.jdbctemplate = jdbcTemplate;
        }

        @Test
        void savePersistantJobInPostgres() {
                Job job = new Job("email-send",
                                Map.of("recipient", "alice@example.com"),
                                Instant.now());

                jobrepository.save(job);

                Integer count = jdbctemplate.queryForObject("SELECT COUNT (*) FROM jobs WHERE id = ?",
                                Integer.class,
                                job.getId());

                assertEquals(1, count);

                String recipient = jdbctemplate.queryForObject("SELECT payload->>'recipient' FROM jobs WHERE id = ?",
                                String.class,
                                job.getId());

                assertEquals(job.getPayload().get("recipient"), recipient);

                String type = jdbctemplate.queryForObject("SELECT type FROM jobs WHERE id = ?",
                                String.class,
                                job.getId());
                assertEquals(job.getType(), type);
        }

        @Test
        void findByIdRestoresPersistedJob() {
                Job job = new Job("email-send",
                                Map.of("recipient", "alice@example.com"),
                                Instant.now());

                job.start(job.getCreatedAt());

                jobrepository.save(job);

                Optional<Job> result = jobrepository.findById(job.getId());
                Job restored = result.orElseThrow();

                assertEquals(restored.getId(), job.getId());
                assertEquals(restored.getStatus(), job.getStatus());
                assertEquals(job.getId(), restored.getId());
                assertEquals(job.getStatus(), restored.getStatus());
                assertEquals(job.getAttempts(), restored.getAttempts());
                assertEquals(job.getType(), restored.getType());
                assertEquals(job.getPayload(), restored.getPayload());
                assertEquals(job.getMaxAttempts(), restored.getMaxAttempts());

        }

        @Test
        void findByIdReturnsEmptyWhenJobDoesNotExist() {
                UUID unknownId = UUID.randomUUID();

                Optional<Job> result = jobrepository.findById(unknownId);

                assertTrue(result.isEmpty());
        }

}