package com.barista.infrastructure.database;

import com.barista.application.job.JobRepository;
import com.barista.job.Job;
import com.barista.job.JobStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.type.TypeReference;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcJobRepository implements JobRepository {

        private final JdbcTemplate jdbcTemplate;
        private final JsonMapper jsonMapper;

        public JdbcJobRepository(
                        JdbcTemplate jdbcTemplate,
                        JsonMapper jsonMapper) {

                this.jdbcTemplate = jdbcTemplate;
                this.jsonMapper = jsonMapper;
        }

        @Override
        public void save(Job job) {

                String sql = """
                                INSERT INTO jobs
                                     (id, type, payload, status,
                                     attempts, max_attempts,
                                     created_at, available_at)
                                VALUES (?, ?, ?::jsonb, ?, ?, ?, ?, ?)
                                """;

                String payloadJson = jsonMapper.writeValueAsString(job.getPayload());

                jdbcTemplate.update(
                                sql,
                                job.getId(),
                                job.getType(),
                                payloadJson,
                                job.getStatus().name(),
                                job.getAttempts(),
                                job.getMaxAttempts(),
                                OffsetDateTime.ofInstant(
                                                job.getCreatedAt(), ZoneOffset.UTC),
                                OffsetDateTime.ofInstant(
                                                job.getAvailableAt(), ZoneOffset.UTC));
        }

        @Override
        public Optional<Job> findById(UUID id) {

                String sql = """
                                SELECT *
                                FROM jobs
                                WHERE id = ?
                                """;

                List<Job> jobs = jdbcTemplate.query(
                                sql,
                                (rs, rowNum) -> {

                                        UUID storedId = rs.getObject("id", UUID.class);

                                        String storedType = rs.getString("type");

                                        JobStatus storedStatus = JobStatus.valueOf(
                                                        rs.getString("status"));

                                        String storedJson = rs.getString("payload");

                                        Map<String, Object> storedPayload = jsonMapper.readValue(
                                                        storedJson,
                                                        new TypeReference<Map<String, Object>>() {
                                                        });

                                        int storedAttempts = rs.getInt("attempts");

                                        int storedMaxAttempts = rs.getInt("max_attempts");

                                        Instant storedCreatedAt = rs.getObject(
                                                        "created_at",
                                                        OffsetDateTime.class).toInstant();

                                        Instant storedAvailableAt = rs.getObject(
                                                        "available_at",
                                                        OffsetDateTime.class).toInstant();

                                        return Job.restore(
                                                        storedId,
                                                        storedType,
                                                        storedPayload,
                                                        storedStatus,
                                                        storedAttempts,
                                                        storedMaxAttempts,
                                                        storedCreatedAt,
                                                        storedAvailableAt);
                                },
                                id);

                return jobs.stream().findFirst();
        }
}
