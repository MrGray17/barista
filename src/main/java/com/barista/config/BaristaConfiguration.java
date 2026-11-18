package com.barista.config;

import org.springframework.context.annotation.Configuration;

import com.barista.application.job.DefaultFailureClassifier;
import com.barista.application.job.FailureClassifier;
import com.barista.application.job.HandleJobFailure;
import com.barista.job.RetryPolicy;

import java.time.Clock;
import java.time.Duration;

import org.springframework.context.annotation.Bean;

@Configuration
public class BaristaConfiguration {

    @Bean
    public FailureClassifier failureClassifier() {
        return new DefaultFailureClassifier();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        return new RetryPolicy(Duration.ofSeconds(2),
                Duration.ofMinutes(1));
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public HandleJobFailure handleJobFailure(
            FailureClassifier failureClassifier,
            RetryPolicy retryPolicy,
            Clock clock) {

        return new HandleJobFailure(
                failureClassifier,
                retryPolicy,
                clock);
    }

}