package com.barista.job;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RetryPolicyTest {

    @Test
    void firstAttemptUsesInitialDelay() {
        RetryPolicy policy = new RetryPolicy(
                Duration.ofSeconds(10),
                Duration.ofMinutes(5));

        assertEquals(
                Duration.ofSeconds(10),
                policy.calculateDelay(1));
    }

    @Test
    void delayDoublesForSubsequentAttempts() {
        RetryPolicy policy = new RetryPolicy(
                Duration.ofSeconds(10),
                Duration.ofMinutes(5));

        assertEquals(Duration.ofSeconds(20), policy.calculateDelay(2));
        assertEquals(Duration.ofSeconds(40), policy.calculateDelay(3));
    }

    @Test
    void delayNeverExceedsMaximumDelay() {
        RetryPolicy policy = new RetryPolicy(
                Duration.ofSeconds(10),
                Duration.ofSeconds(25));

        assertEquals(Duration.ofSeconds(25), policy.calculateDelay(3));
        assertEquals(Duration.ofSeconds(25), policy.calculateDelay(4));
    }

    @Test
    void rejectsInvalidAttemptNumbers() {
        RetryPolicy policy = new RetryPolicy(
                Duration.ofSeconds(10),
                Duration.ofMinutes(5));

        assertThrows(
                IllegalArgumentException.class,
                () -> policy.calculateDelay(0));

        assertThrows(
                IllegalArgumentException.class,
                () -> policy.calculateDelay(-1));
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RetryPolicy(
                        Duration.ZERO,
                        Duration.ofSeconds(10)));

        assertThrows(
                IllegalArgumentException.class,
                () -> new RetryPolicy(
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(5)));

        assertThrows(
                NullPointerException.class,
                () -> new RetryPolicy(null, Duration.ofSeconds(10)));
    }

    @Test
    void exponentialCalculationDoesNotOverflow() {
        Duration initialDelay = Duration.ofSeconds(
                Long.MAX_VALUE / 2 + 1);
        Duration maxDelay = Duration.ofSeconds(Long.MAX_VALUE);

        RetryPolicy policy = new RetryPolicy(initialDelay, maxDelay);

        assertEquals(maxDelay, policy.calculateDelay(2));
    }
}